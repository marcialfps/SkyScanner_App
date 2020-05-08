package com.miw.skyscanner.ui.flights

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.miw.skyscanner.ui.flights.list.FlightsListFragment

class FlightsCollectionAdapter (activity: FragmentActivity, private val itemsCount: Int) :
    FragmentStateAdapter(activity) {

    var currentFragment: Int = 0
    var innerFragments: MutableList<FlightsListFragment> = mutableListOf()

    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 -> FlightsListFragment(isArrivals = true)
            else -> FlightsListFragment(isArrivals = false)
        }
        innerFragments.add(fragment)
        return fragment
    }
}