package com.codding.test.startoverflowuser.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import com.codding.test.startoverflowuser.R
import com.codding.test.startoverflowuser.eventbus.MessageEvent
import com.codding.test.startoverflowuser.util.AppLogger
import com.codding.test.startoverflowuser.util.EventMessage
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class FlashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flash_screen)
        EventBus.getDefault().register(this)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        // Block back key at this time
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        AppLogger.debug(this, "onMessageEvent $event.messageCode")
        if (event.messageCode == EventMessage.LOAD_DATA_COMPLETE) {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
