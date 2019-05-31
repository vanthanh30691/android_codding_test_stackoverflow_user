package com.codding.test.startoverflowuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codding.test.startoverflowuser.R
import kotlinx.android.synthetic.main.loading_row.view.*

class ReputationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reputation)
    }

    override fun setupProgessBar() {
        processBar = findViewById(R.id.processBar)
    }
}
