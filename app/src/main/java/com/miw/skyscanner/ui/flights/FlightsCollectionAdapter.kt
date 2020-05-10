package com.miw.skyscanner.ui.flights

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.miw.skyscanner.ui.flights.list.FlightsListFragment

class FlightsCollectionAdapter (val parent: FlightsFragment, private val itemsCount: Int) :
    FragmentStateAdapter(parent) {

    var currentFragmentIndex: Int = 0
    var innerFragments: MutableList<FlightsListFragment> = mutableListOf()
    var currentFragment: () -> FlightsListFragment? = {
        if (innerFragments.isNotEmpty())
            innerFragments[currentFragmentIndex]
        else null
    }

    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 -> FlightsListFragment(true, this)
            else -> FlightsListFragment(false, this)
        }
        innerFragments.add(fragment)
        return fragment
    }
}