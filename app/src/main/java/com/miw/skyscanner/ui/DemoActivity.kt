package com.miw.skyscanner.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import com.miw.skyscanner.R
import com.miw.skyscanner.ui.flights.FlightsFragment
import kotlinx.android.synthetic.main.activity_demo.*

class DemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        val view = supportFragmentManager.beginTransaction().add(demoContainer.id, FlightsFragment())
            .commit()
    }
}
