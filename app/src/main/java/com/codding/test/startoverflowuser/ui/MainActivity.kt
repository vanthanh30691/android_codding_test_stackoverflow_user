package com.codding.test.startoverflowuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codding.test.startoverflowuser.interator.SoFListIterator
import com.codding.test.startoverflowuser.listener.SofUserRowListener
import com.codding.test.startoverflowuser.screenstate.ScreenState
import com.codding.test.startoverflowuser.screenstate.SoFListState
import com.codding.test.startoverflowuser.ui.adapter.SofListAdapter
import com.codding.test.startoverflowuser.viewmodal.SoFListViewModal
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.codding.test.startoverflowuser.R;
import com.codding.test.startoverflowuser.ui.adapter.RVEmptyObserver
import com.codding.test.startoverflowuser.util.AppLogger
import com.codding.test.startoverflowuser.util.Constant
import com.codding.test.startoverflowuser.util.NetWorkConnectionState
import com.codding.test.startoverflowuser.util.getConnectionType
import com.codding.test.startoverflowuser.viewmodal.SoFListViewModalFactory
import kotlinx.android.synthetic.main.bacsic_recycler_view_content.*


class MainActivity : BaseActivity() {

    // View groups item
    private lateinit var viewModal : SoFListViewModal
    private lateinit var favoriteButton : FloatingActionButton
    private lateinit var sofListView : RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var emptyView : TextView

    // Variable items
    private lateinit var sofListAdapter : SofListAdapter
    private var isFetchingMoreData  = false
    private var lastConnectionType = NetWorkConnectionState.NONE
    private var isFavoriteMode = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupProgessBar()

        // Inflate view
        favoriteButton = findViewById(R.id.fab)
        sofListView = findViewById(R.id.recyViewSofList)
        emptyView = findViewById(R.id.emptyView)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)


        // Init variables
        viewModal = ViewModelProviders.of(this,
            SoFListViewModalFactory(SoFListIterator(application)))[SoFListViewModal::class.java]
        viewModal.sofListState.observe(::getLifecycle, ::updateUI)

        sofListAdapter = SofListAdapter()

        // Setup listener
        swipeRefreshLayout.setOnRefreshListener {
            sofListAdapter.getData().clear()
            viewModal.refreshData(isFavoriteMode)
        }
        favoriteButton.setOnClickListener { toogleFavoriteMode() }
        sofListAdapter.setUserRowListener(object : SofUserRowListener {
            override fun onUserClicked(position: Int) {
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
    }

    override fun setupProgessBar() {
        processBar = findViewById(R.id.processBar)
    }

    private fun toogleFavoriteMode() {
        AppLogger.debug(this, "toogleFavoriteMode")
        AppLogger.debug(this, isFavoriteMode)

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

    private fun toogleUserFafovirteAt(position: Int) {
        AppLogger.debug(this, "toogleUserFafovirteAt")
        AppLogger.debug(this, position)

        var sofUser = sofListAdapter.getItemAt(position)
        sofUser?. let {
            viewModal.toogleFavoriteState(sofUser)
        }
    }

    private fun fetchMoreData() {
        AppLogger.debug(this, "fetchMoreData")

        lastConnectionType = getConnectionType(this)
        when(lastConnectionType) {
            NetWorkConnectionState.NONE -> {
                isFetchingMoreData = false
                Toast.makeText(this, R.string.error_no_internet_connection, Toast.LENGTH_LONG).show()
            }
            NetWorkConnectionState.CELL -> viewModal.getSofUser(Constant.SOF_DATA_LOAD_PAGE_SIZE_ON_WIFI)
            NetWorkConnectionState.WIFI -> viewModal.getSofUser(Constant.SOF_DATA_LOAD_PAGE_SIZE)
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
        AppLogger.debug(this, "processLoadDataState")
        AppLogger.debug(this, sofListState)

        isFetchingMoreData = false
        swipeRefreshLayout.isRefreshing = false

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
        recyViewSofList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                            fetchMoreData()
                            isFetchingMoreData = true
                        }
                    }
                }
            }
        })

    }

}
