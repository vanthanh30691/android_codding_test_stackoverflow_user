package com.codding.test.startoverflowuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.solver.widgets.ConstraintAnchor
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
import com.codding.test.startoverflowuser.viewmodal.SoFListViewModalFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.codding.test.startoverflowuser.R;
import com.codding.test.startoverflowuser.util.Constant
import com.codding.test.startoverflowuser.util.NetWorkConnectionState
import com.codding.test.startoverflowuser.util.getConnectionType
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    // View groups item
    private lateinit var viewModal : SoFListViewModal
    private lateinit var processBar : ProgressBar
    private lateinit var favoriteButton : FloatingActionButton
    private lateinit var sofListView : RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    // Variable items
    private lateinit var sofListAdapter : SofListAdapter
    private var isFetchingData  = false
    private var lastConnectionType = NetWorkConnectionState.NONE


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inflate view
        processBar = findViewById(R.id.processBar)
        favoriteButton = findViewById(R.id.fab)
        sofListView = findViewById(R.id.recyViewSofList)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)


        // Init variables
        viewModal = ViewModelProviders.of(this, SoFListViewModalFactory(SoFListIterator()))[SoFListViewModal::class.java]
        viewModal.sofListState.observe(::getLifecycle, ::updateUI)
        sofListAdapter = SofListAdapter()

        // Setup listener
        swipeRefreshLayout.setOnRefreshListener {
            fetchMoreData()
            swipeRefreshLayout.isRefreshing = false
        }
        favoriteButton.setOnClickListener { }
        sofListAdapter.setUserRowListener(object : SofUserRowListener {
            override fun onUserClicked(position: Int) {
            }

            override fun onUserFavoritedTogle(position: Int) {
            }
        })

        // Setup view/variable state
        sofListView.layoutManager = LinearLayoutManager(this)
        sofListView.adapter = sofListAdapter


        // Setup header title
        supportActionBar?.let {
            it.title = getString(R.string.header_stack_overflow_user)
        }

        initScrollListener()
        fetchMoreData()

    }

    private fun fetchMoreData() {
        lastConnectionType = getConnectionType(this)
        when(lastConnectionType) {
            NetWorkConnectionState.NONE -> Toast.makeText(this, R.string.error_no_internet_connection, Toast.LENGTH_LONG).show()
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

    private fun processLoadDataState(sofListState : SoFListState) {
        isFetchingData = false
        hideLoadingScreen()
        when (sofListState) {
            SoFListState.LoadUserDone -> {
                var sofUser = viewModal.sofUser
                sofListAdapter.addMoreData(sofUser)
            }
        }
    }

    private fun showingLoadingScreen() {
        processBar.visibility = View.VISIBLE
    }

    private fun hideLoadingScreen() {
        processBar.visibility = View.GONE
    }

    private fun initScrollListener() {
        recyViewSofList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?

                if (!isFetchingData) {
                    if (linearLayoutManager != null) {

                        var currentItemSize = sofListAdapter.itemCount
                        var lastItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                        var numberItemToReachBottom = currentItemSize - lastItemPosition

                        Log.d("TEST", "numberItemToReachBottom: " + numberItemToReachBottom)
                        // In case user on wifi and there have Constant.SOF_DATA_BACKGROUND_LOAD_PADDING to reach bottom
                        // Fetch data in background or  in case data list reached bottom
                        if (numberItemToReachBottom <= 1
                            || (numberItemToReachBottom < Constant.SOF_DATA_BACKGROUND_LOAD_PADDING
                            && lastConnectionType == NetWorkConnectionState.WIFI)) {
                            Log.d("TEST", "fetchMoreData: ")
                            fetchMoreData()
                            isFetchingData = true
                        }
                    }
                }
            }
        })

    }
}
