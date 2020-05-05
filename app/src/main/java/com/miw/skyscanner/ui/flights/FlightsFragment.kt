package com.miw.skyscanner.ui.flights

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.miw.skyscanner.R
import kotlinx.android.synthetic.main.fragment_flights.*


private const val NUMBER_OF_TABS = 2

class FlightsFragment : Fragment() {

    private lateinit var tabNames: Map<Int, String>
    private lateinit var flightsCollectionAdapter: FlightsCollectionAdapter

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
        setUpViewPager()
        setUpTabs()
        setUpRefresh()
    }

    private fun setUpViewPager () {
        if (activity != null){
            flightsCollectionAdapter = FlightsCollectionAdapter(activity!!, NUMBER_OF_TABS)
            flightsViewPager.adapter = flightsCollectionAdapter

            // Pager events
            flightsViewPager.registerOnPageChangeCallback (
                object : ViewPager2.OnPageChangeCallback() {

                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {
                        super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                        // Do not allow refresh while changing tabs for usability
                        when (0.0.compareTo(positionOffset) == 0){
                            true -> flightsSwipeLayout.isEnabled = true
                            false -> flightsSwipeLayout.isEnabled = false
                        }
                    }

                    override fun onPageSelected(position: Int) {
                        Toast.makeText(
                            activity, "Tab: ${tabNames[position]}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
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

        flightsSwipeLayout.setOnRefreshListener {
            onRefreshed()
            flightsSwipeLayout.isRefreshing = false
        }
    }

    private fun onRefreshed (): Boolean {
        Toast.makeText(activity, "Refreshed",
            Toast.LENGTH_SHORT).show()
        return true
    }

}