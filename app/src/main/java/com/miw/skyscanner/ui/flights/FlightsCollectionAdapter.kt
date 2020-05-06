package com.miw.skyscanner.ui.flights

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.miw.skyscanner.ui.flights.list.FlightsListFragment

class FlightsCollectionAdapter (activity: FragmentActivity, private val itemsCount: Int) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FlightsListFragment(isArrivals = true) // Arrivals list
            else -> FlightsListFragment(isArrivals = false) // Departures list
        }
    }
}