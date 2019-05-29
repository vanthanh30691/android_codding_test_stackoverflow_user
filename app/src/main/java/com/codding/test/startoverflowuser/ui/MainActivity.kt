package com.codding.test.startoverflowuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codding.test.startoverflowuser.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Setup toolbar and header title
        supportActionBar?.let {
            it.title = getString(R.string.header_stack_overflow_user)
        }
    }
}
