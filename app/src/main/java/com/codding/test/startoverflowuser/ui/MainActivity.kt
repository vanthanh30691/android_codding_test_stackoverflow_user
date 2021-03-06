package com.codding.test.startoverflowuser.ui

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codding.test.startoverflowuser.listener.SofUserRowListener
import com.codding.test.startoverflowuser.screenstate.ScreenState
import com.codding.test.startoverflowuser.screenstate.SoFListState
import com.codding.test.startoverflowuser.ui.adapter.SofListAdapter
import com.codding.test.startoverflowuser.viewmodal.SoFListViewModal
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.codding.test.startoverflowuser.R;
import com.codding.test.startoverflowuser.SofApplication
import com.codding.test.startoverflowuser.ui.adapter.RVEmptyObserver
import com.codding.test.startoverflowuser.util.*
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.bacsic_recycler_view_content.*
import timber.log.Timber
import javax.inject.Inject


class MainActivity : BaseActivity() {

    // View groups item
    private lateinit var viewModal : SoFListViewModal
    private lateinit var favoriteButton : FloatingActionButton
    private lateinit var sofListView : RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var emptyView : TextView

    // Variable items
    private var isFetchingMoreData  = false

    private var isFavoriteMode = false
    // Monitor current state and network state to reload data automatically when network on
    private var  currentState : SoFListState = SoFListState.StartLoadNewUser
    private var lastConnectionType = NetWorkConnectionState.NONE

    @Inject
    lateinit var sofListAdapter : SofListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupProgessBar()

        // Inflate view
        favoriteButton = findViewById(R.id.fab)
        sofListView = findViewById(R.id.recyView)
        emptyView = findViewById(R.id.emptyView)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)


        // Init variables
        viewModal = ViewModelProvider.AndroidViewModelFactory.getInstance(application).
            create(SoFListViewModal::class.java)
        viewModal.modalState.observe(::getLifecycle, ::updateUI)

        // Setup listener
        swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
        favoriteButton.setOnClickListener { toogleFavoriteMode() }
        sofListAdapter.setUserRowListener(object : SofUserRowListener {
            override fun onUserClicked(position: Int) {
                sofListAdapter.getItemAt(position)?.let {
                    startRepuActivity(it.userId)
                }
            }

            override fun onUserFavoritedTogle(position: Int) {
                toogleUserFafovirteAt(position)
            }
        })

        // Setup view/variable state
        sofListView.layoutManager = LinearLayoutManager(this)
        sofListView.adapter = sofListAdapter
        sofListAdapter.registerAdapterDataObserver(RVEmptyObserver(sofListView, emptyView))


        // Setup header title
        supportActionBar?.let {
            it.title = getString(R.string.header_stack_overflow_user)
        }

        initScrollListener()
        fetchMoreData()
        // Start flash screen and wait load data complete
        startActivityForResult(Intent(this, FlashScreenActivity::class.java), IntentCons.INTENT_REQUEST_LOADDING_SCREEN)

    }

    override fun setupProgessBar() {
        processBar = findViewById(R.id.processBar)
    }

    override fun onNetworkAvailable() {
        Timber.d("onNetworkAvailable $currentState")
        when (currentState) {
            // Resume load data again from fail states
            SoFListState.LoadFavoriteListDone, SoFListState.LoadUserError ->  fetchMoreData()
        }
    }

    private fun refreshData() {
        sofListAdapter.getData().clear()
        viewModal.refreshData(isFavoriteMode)
    }

    private fun toogleFavoriteMode() {
        Timber.d("toogleFavoriteMode: $isFavoriteMode")
        isFavoriteMode = !isFavoriteMode
        // Reset adapter data
        sofListAdapter.toogleAdapterMode(isFavoriteMode)

        if (isFavoriteMode) {
            favoriteButton.setImageResource(R.drawable.favorite_checked)
            viewModal.getFavoriteUser()
        } else {
            favoriteButton.setImageResource(R.drawable.favorite)
            fetchMoreData()
        }
    }

    private fun startRepuActivity(userId : String) {
        Timber.d("startRepuActivity $userId")
        var intent = Intent(this, ReputationActivity::class.java)
        intent.putExtra(IntentCons.INTENT_USER_ID, userId)
        startActivity(intent)
    }

    private fun toogleUserFafovirteAt(position: Int) {
        Timber.d("toogleUserFafovirteAt $position")

        var sofUser = sofListAdapter.getItemAt(position)
        sofUser?. let {
            viewModal.toogleFavoriteState(sofUser)
        }
    }

    private fun fetchMoreData() {
        Timber.d("fetchMoreData")
        lastConnectionType = getConnectionType(this)

        Timber.d("$lastConnectionType")
        when(lastConnectionType) {
            NetWorkConnectionState.NONE -> {
                isFetchingMoreData = false
                currentState = SoFListState.LoadUserError
                Toast.makeText(this, R.string.error_no_internet_connection, Toast.LENGTH_LONG).show()
            }
            else -> {
                isFetchingMoreData = true
                viewModal.getSofUser(getPageSize())
            }

        }
    }

    private fun updateUI(screenState : ScreenState<SoFListState>?) {
        when (screenState) {
            ScreenState.Loading -> showingLoadingScreen()
            is ScreenState.Render -> processLoadDataState(screenState.renderState)
        }
    }

    /**
     * Controll UI State
     */
    private fun processLoadDataState(sofListState : SoFListState) {
        Timber.d("processLoadDataState with state: $sofListState")
        isFetchingMoreData = false

        swipeRefreshLayout.isRefreshing = false
        currentState = sofListState

        when (sofListState) {
            SoFListState.LoadUserDone -> {
                hideLoadingScreen()
                var sofUser = viewModal.sofUser
                sofListAdapter.addMoreData(sofUser)
            }
            // Build favorite list first
            SoFListState.LoadFavoriteListDone -> {
                sofListAdapter.buildFavoriteList(viewModal.favoriteSofUserIdList)
            }

            SoFListState.ReachedOutOfData -> {
                hideLoadingScreen()
                Toast.makeText(this, R.string.error_reached_end_off_data, Toast.LENGTH_SHORT).show()
            }

            SoFListState.LoadUserError -> {
                hideLoadingScreen()
                Toast.makeText(this, R.string.error_loadding_data, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initScrollListener() {
        recyView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                // Only lazy load in online mode
                if (!isFetchingMoreData && !isFavoriteMode) {
                    if (linearLayoutManager != null) {

                        var currentItemSize = sofListAdapter.itemCount
                        var lastItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                        var numberItemToReachBottom = currentItemSize - lastItemPosition

                        // In case user on wifi and there have Constant.SOF_DATA_BACKGROUND_LOAD_PADDING to reach bottom
                        // Fetch data in background or  in case data list reached bottom
                        if (numberItemToReachBottom <= 1
                            || (numberItemToReachBottom < Constant.SOF_DATA_BACKGROUND_LOAD_PADDING
                            && lastConnectionType == NetWorkConnectionState.WIFI)) {
                            Timber.d("onScrolled to end, load more data")
                            fetchMoreData()
                        }
                    }
                }
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IntentCons.INTENT_REQUEST_LOADDING_SCREEN) {
            if (resultCode == IntentCons.INTENT_RESULT_EXIT_APP) {
                finish()
            } else {
                // Load data done, load favorite user id list
                viewModal.getFavoriteIdList()
            }
        }
    }

}
