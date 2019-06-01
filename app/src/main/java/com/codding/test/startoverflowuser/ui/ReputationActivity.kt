package com.codding.test.startoverflowuser.ui

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codding.test.startoverflowuser.R
import com.codding.test.startoverflowuser.interator.ReputationIterator
import com.codding.test.startoverflowuser.screenstate.ReputationState
import com.codding.test.startoverflowuser.screenstate.ScreenState
import com.codding.test.startoverflowuser.screenstate.SoFListState
import com.codding.test.startoverflowuser.ui.adapter.RVEmptyObserver
import com.codding.test.startoverflowuser.ui.adapter.RepuListAdapter
import com.codding.test.startoverflowuser.util.*
import com.codding.test.startoverflowuser.viewmodal.ReputationViewModal
import com.codding.test.startoverflowuser.viewmodal.ReputationViewModalFactory
import kotlinx.android.synthetic.main.bacsic_recycler_view_content.*

class ReputationActivity : BaseActivity() {

    // View groups item
    private lateinit var viewModal : ReputationViewModal
    private lateinit var repuListView : RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var emptyView : TextView

    // Variable items
    private lateinit var repuAdapter : RepuListAdapter
    private lateinit var userId : String
    private var isFetchingMoreData  = false
    private var lastConnectionType = NetWorkConnectionState.NONE

    // Monitor current state and network state to reload data automatically when network on
    private var  currentState : ReputationState = ReputationState.StartLoadReputation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reputation)
        setupProgessBar()

        // Inflate view
        repuListView = findViewById(R.id.recyView)
        emptyView = findViewById(R.id.emptyView)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)


        // Init variables
        viewModal = ViewModelProviders.of(this,
            ReputationViewModalFactory(ReputationIterator(application))
        )[ReputationViewModal::class.java]
        viewModal.modalState.observe(::getLifecycle, ::updateUI)
        repuAdapter = RepuListAdapter()

        // Setup listener
        swipeRefreshLayout.setOnRefreshListener {
            repuAdapter.getData().clear()
            viewModal.refreshData()
        }


        // Setup view/variable state
        repuListView.layoutManager = LinearLayoutManager(this)
        repuListView.adapter = repuAdapter
        repuAdapter.registerAdapterDataObserver(RVEmptyObserver(repuListView, emptyView))

        // Setup header title
        supportActionBar?.let {
            it.title = getString(R.string.header_stack_overflow_reputation)
            it.setDisplayHomeAsUpEnabled(true)
        }

        // Get user id from intent
        intent?.let {
            userId = it.getStringExtra(IntentCons.INTENT_USER_ID)
            AppLogger.debug(this@ReputationActivity, "get Intent UserId: $userId")
        }

        initScrollListener()
        fetchMoreData()

    }

    override fun onNetworkAvailable() {
        AppLogger.debug(this, "onNetworkAvailable $currentState")
        when (currentState) {
            // Resume load data again from fail states
            ReputationState.LoadRepuError ->  fetchMoreData()
        }
    }

    private fun fetchMoreData() {
        AppLogger.debug(this, "fetchMoreData")

        lastConnectionType = getConnectionType(this)
        when(lastConnectionType) {
            NetWorkConnectionState.NONE -> {
                isFetchingMoreData = false
                currentState = ReputationState.LoadRepuError
                Toast.makeText(this, R.string.error_no_internet_connection, Toast.LENGTH_LONG).show()
            }
            else -> {
                isFetchingMoreData = true
                viewModal.getReputationList(userId, getPageSize())
            }
        }
    }

    private fun updateUI(screenState : ScreenState<ReputationState>?) {
        when (screenState) {
            ScreenState.Loading -> showingLoadingScreen()
            is ScreenState.Render -> processLoadDataState(screenState.renderState)
        }
    }

    /**
     * Controll UI State
     */
    private fun processLoadDataState(repuState : ReputationState) {
        AppLogger.debug(this, "processLoadDataState")
        AppLogger.debug(this, repuState)

        isFetchingMoreData = false
        swipeRefreshLayout.isRefreshing = false
        currentState = repuState

        when (repuState) {
            ReputationState.LoadRepuDone -> {
                hideLoadingScreen()
                var repuList = viewModal.repuList
                repuAdapter.addMoreData(repuList)
            }

            ReputationState.ReachedOutOfData -> {
                hideLoadingScreen()
                Toast.makeText(this, R.string.error_reached_end_off_data, Toast.LENGTH_SHORT).show()
            }

            ReputationState.LoadRepuError -> {
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
                if (!isFetchingMoreData){
                    if (linearLayoutManager != null) {

                        var currentItemSize = repuAdapter.itemCount
                        var lastItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                        var numberItemToReachBottom = currentItemSize - lastItemPosition

                        // In case user on wifi and there have Constant.SOF_DATA_BACKGROUND_LOAD_PADDING to reach bottom
                        // Fetch data in background or  in case data list reached bottom
                        if (numberItemToReachBottom <= 1
                            || (numberItemToReachBottom < Constant.SOF_DATA_BACKGROUND_LOAD_PADDING
                                    && lastConnectionType == NetWorkConnectionState.WIFI)) {
                            fetchMoreData()

                        }
                    }
                }
            }
        })

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?. let {
            if (it.itemId == android.R.id.home) {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun setupProgessBar() {
        processBar = findViewById(R.id.processBar)
    }

}
