package com.codding.test.startoverflowuser.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.ProgressBar
import com.codding.test.startoverflowuser.util.AppLogger

abstract class BaseActivity : AppCompatActivity() {

    var processBar : ProgressBar? = null

    /**
     * Setup processBar variable to using hide/showLoadingScreen
     */
    protected abstract fun setupProgessBar()


    protected fun showingLoadingScreen() {
        AppLogger.debug(this, "showingLoadingScreen")
        processBar?. let {
            it.visibility = View.VISIBLE
        }
    }

    protected fun hideLoadingScreen() {
        AppLogger.debug(this, "hideLoadingScreen")
        processBar?. let {
            it.visibility = View.GONE
        }
    }

}
