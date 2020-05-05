package com.miw.skyscanner.ui.flights

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.miw.skyscanner.R
import com.miw.skyscanner.ui.DemoFragment

class FlightsCollectionAdapter (activity: FragmentActivity, private val itemsCount: Int) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DemoFragment(R.layout.fragment_main_departures)
            else -> DemoFragment(R.layout.fragment_main_arrivals)
        }
    }
}