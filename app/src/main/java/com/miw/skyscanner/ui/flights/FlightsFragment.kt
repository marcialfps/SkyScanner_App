package com.miw.skyscanner.ui.flights

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.miw.skyscanner.R
import kotlinx.android.synthetic.main.fragment_flights.*


private const val NUMBER_OF_TABS = 2

class FlightsFragment : Fragment() {

    private lateinit var tabNames: Map<Int, String>
    private lateinit var flightsCollectionAdapter: FlightsCollectionAdapter
    // Event handlers for the tabs
    private val flightsPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {

        override fun onPageScrollStateChanged(state: Int) {
            // Do not allow refresh while changing tabs for usability
            val enabled: Boolean = state == ViewPager2.SCROLL_STATE_IDLE
            flightsSwipeLayout?.isEnabled = enabled
        }

        override fun onPageSelected(position: Int) {
            flightsCollectionAdapter.currentFragmentIndex = position
        }
    }

    // Inflate the fragment containing the ViewPager and the arrivals/departures tabs
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_flights, container, false)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        flightsViewPager.unregisterOnPageChangeCallback(flightsPageChangeCallback)
    }

    // Setup the fragment
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewPager()
        setUpTabs()
        setUpRefresh()
    }

    private fun setUpViewPager () {
        if (activity != null){
            flightsCollectionAdapter = FlightsCollectionAdapter(this, NUMBER_OF_TABS)
            flightsViewPager.adapter = flightsCollectionAdapter
            flightsViewPager.registerOnPageChangeCallback (flightsPageChangeCallback)
        }
    }

    private fun setUpTabs () {
        tabNames = mapOf(
            0 to getString(R.string.arrivals_arrivals),
            1 to getString(R.string.departures_departures)
        )

        TabLayoutMediator(flightsTabLayout, flightsViewPager) { tab, position ->
            tab.text = tabNames[position]
        }.attach()
    }

    private fun setUpRefresh () {

        flightsSwipeLayout?.setOnRefreshListener {
            onRefreshStart()
        }
    }

    private fun onRefreshStart () {
        // Request information again
        flightsCollectionAdapter.currentFragment()?.fetchFlights(forceQueryServer = true)
    }

    fun onRefreshEnd () {
        // Stop refresh spinner
        flightsSwipeLayout?.isRefreshing = false
    }

}
