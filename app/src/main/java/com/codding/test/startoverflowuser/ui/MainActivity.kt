package com.codding.test.startoverflowuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codding.test.startoverflowuser.R
import com.codding.test.startoverflowuser.interator.SoFListIterator
import com.codding.test.startoverflowuser.listener.SofUserRowListener
import com.codding.test.startoverflowuser.screenstate.ScreenState
import com.codding.test.startoverflowuser.screenstate.SoFListState
import com.codding.test.startoverflowuser.ui.adapter.SofListAdapter
import com.codding.test.startoverflowuser.viewmodal.SoFListViewModal
import com.codding.test.startoverflowuser.viewmodal.SoFListViewModalFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    // View groups item
    private lateinit var viewModal : SoFListViewModal
    private lateinit var processBar : ProgressBar
    private lateinit var favoriteButton : FloatingActionButton
    private lateinit var sofListView : RecyclerView

    // Variable items
    private lateinit var sofListAdapter : SofListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inflate view
        processBar = findViewById(R.id.processBar)
        favoriteButton = findViewById(R.id.fab)
        sofListView = findViewById(R.id.recyViewSofList)


        // Init variables
        viewModal = ViewModelProviders.of(this, SoFListViewModalFactory(SoFListIterator()))[SoFListViewModal::class.java]
        viewModal.sofListState.observe(::getLifecycle, ::updateUI)
        sofListAdapter = SofListAdapter()

        // Setup listener
        favoriteButton.setOnClickListener { viewModal.getSofUser() }
        sofListAdapter.setUserRowListener(object : SofUserRowListener {
            override fun onUserClicked(position: Int) {
                Log.d("TEST", "onUserClicked on: " + position)
            }

            override fun onUserFavoritedTogle(position: Int) {
                Log.d("TEST", "onUserFavoritedTogle on: " + position)
            }
        })

        // Setup view/variable state
        sofListView.layoutManager = LinearLayoutManager(this)
        sofListView.adapter = sofListAdapter


        // Setup header title
        supportActionBar?.let {
            it.title = getString(R.string.header_stack_overflow_user)
        }

    }

    private fun updateUI(screenState : ScreenState<SoFListState>?) {
        when (screenState) {
            ScreenState.Loading -> showingLoadingScreen()
            is ScreenState.Render -> processLoadDataState(screenState.renderState)
        }
    }

    private fun processLoadDataState(sofListState : SoFListState) {
        hideLoadingScreen()
        when (sofListState) {
            SoFListState.LoadUserDone -> {
                var sofUser = viewModal.sofUser
                sofListAdapter.setAllSofData(sofUser)
            }
        }
    }

    private fun showingLoadingScreen() {
        processBar.visibility = View.VISIBLE
    }

    private fun hideLoadingScreen() {
        processBar.visibility = View.GONE
    }
}
