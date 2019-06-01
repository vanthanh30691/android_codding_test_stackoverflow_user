package com.codding.test.startoverflowuser.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.ProgressBar
import com.codding.test.startoverflowuser.screenstate.ScreenState
import com.codding.test.startoverflowuser.screenstate.SoFListState
import com.codding.test.startoverflowuser.util.AppLogger
import com.codding.test.startoverflowuser.util.Constant
import com.codding.test.startoverflowuser.util.NetWorkConnectionState
import com.codding.test.startoverflowuser.util.getConnectionType

abstract class BaseActivity : AppCompatActivity() {

    var processBar : ProgressBar? = null
    private var pageSize = 0

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

    protected fun getPageSize() : Int {
        if (pageSize == 0) {
            when ( getConnectionType(this)) {
                NetWorkConnectionState.CELL -> return Constant.SOF_DATA_LOAD_PAGE_SIZE_ON_WIFI
                NetWorkConnectionState.WIFI -> return Constant.SOF_DATA_LOAD_PAGE_SIZE
            }
        }
        return pageSize
    }



}
