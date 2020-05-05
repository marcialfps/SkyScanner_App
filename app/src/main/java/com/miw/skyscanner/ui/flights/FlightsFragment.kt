package com.miw.skyscanner.ui.flights

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.miw.skyscanner.R
import kotlinx.android.synthetic.main.fragment_flights.*
import com.google.android.material.tabs.TabLayoutMediator


private const val NUMBER_OF_TABS = 2

class FlightsFragment : Fragment() {

    private lateinit var tabNames: Map<Int, String>

    // Inflate the fragment containing the ViewPager and the arrivals/departures tabs
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_flights, container, false)
    }

    // Setup the fragment
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    private fun initialize () {

        // Set up view pager
        if (activity != null){
            val flightsCollectionAdapter = FlightsCollectionAdapter(activity!!, NUMBER_OF_TABS)
            flightsViewPager.adapter = flightsCollectionAdapter
            flightsViewPager.registerOnPageChangeCallback(flightsPageChangeCallback)
        }

        // Set up tabs
        tabNames = mapOf(
            0 to getString(R.string.arrivals_arrivals),
            1 to getString(R.string.departures_departures)
        )

        TabLayoutMediator(flightsTabLayout, flightsViewPager) { tab, position ->
            tab.text = tabNames[position]
        }.attach()

    }

    // Setup listeners for events in the ViewPager
    private var flightsPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            Toast.makeText(activity, "Selected tab: ${tabNames[position]}",
                Toast.LENGTH_SHORT).show()
        }
    }
}
