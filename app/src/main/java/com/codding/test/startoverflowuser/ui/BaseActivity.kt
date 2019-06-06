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
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.ConnectivityManager.NetworkCallback
import timber.log.Timber


abstract class BaseActivity : AppCompatActivity() {

    var processBar : ProgressBar? = null
    private var pageSize = 0
    private lateinit var connectionStateMonitor: ConnectionStateMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectionStateMonitor = ConnectionStateMonitor()
        connectionStateMonitor.enable(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        connectionStateMonitor.disable(this)
    }


    /**
     * Setup processBar variable to using hide/showLoadingScreen
     */
    protected abstract fun setupProgessBar()

    override fun onResume() {
        super.onResume()
        connectionStateMonitor.enable(this)
    }


    protected fun showingLoadingScreen() {
        Timber.d("showingLoadingScreen")
        processBar?. let {
            it.visibility = View.VISIBLE
        }
    }

    protected fun hideLoadingScreen() {
        Timber.d("hideLoadingScreen")
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

    abstract protected fun onNetworkAvailable()

    inner class ConnectionStateMonitor : NetworkCallback() {
         private var networkRequest : NetworkRequest? = null

        init {
            networkRequest = NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build()
        }

        fun enable(context: Context) {
            Timber.d("ConnectionStateMonitor enable")
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.registerNetworkCallback(networkRequest, this)
        }

        fun disable(context: Context) {
            Timber.d("ConnectionStateMonitor disable")
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.unregisterNetworkCallback(this)
        }

        override fun onAvailable(network: Network) {
            Timber.d("ConnectionStateMonitor onAvailable")
            onNetworkAvailable()
        }
    }



}
