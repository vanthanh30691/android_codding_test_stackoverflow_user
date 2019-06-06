package com.codding.test.startoverflowuser.ui

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import com.codding.test.startoverflowuser.R
import com.codding.test.startoverflowuser.eventbus.MessageEvent
import com.codding.test.startoverflowuser.util.AppLogger
import com.codding.test.startoverflowuser.util.EventMessage
import com.codding.test.startoverflowuser.util.IntentCons
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber

class FlashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flash_screen)
        EventBus.getDefault().register(this)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        // Close app if user want
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(IntentCons.INTENT_RESULT_EXIT_APP)
            finish()
        }
        return super.onKeyUp(keyCode, event)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        Timber.d("onMessageEvent $event.messageCode")
        if (event.messageCode == EventMessage.LOAD_DATA_COMPLETE) {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
