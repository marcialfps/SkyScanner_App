package com.miw.skyscanner.ui.flights.list

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.miw.skyscanner.R
import com.miw.skyscanner.model.Plane
import com.miw.skyscanner.ui.flights.FlightsCollectionAdapter
import kotlinx.android.synthetic.main.fragment_flights_list.*
import kotlin.properties.Delegates


class FlightsListFragment (private val isArrivals: Boolean,
                           private val parent: FlightsCollectionAdapter) : Fragment() {

    // List of items that the view must handle
    var planes: List<Plane> by Delegates.observable(listOf()) {
            _, _, newList ->
        if (newList.isEmpty()) notifyError()
        else showFlightList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_flights_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        flightsListRecyclerView.layoutManager = SmoothScrollLayoutManager(context, 30f)
        FetchPlanesTask(this).execute(isArrivals)
    }

    private fun showFlightList () {
        parent.innerFragments[parent.currentFragment]
            .activity?.findViewById<ProgressBar>(R.id.progressBarFlights)?.visibility = View.INVISIBLE
        flightsListRecyclerView.adapter = FlightsListAdapter(activity!!, planes, isArrivals)
    }

    private fun notifyError () {
        parent.innerFragments[parent.currentFragment]
            .activity?.findViewById<TextView>(R.id.txFlightsError)?.visibility = View.VISIBLE
        parent.innerFragments[parent.currentFragment]
            .activity?.findViewById<ProgressBar>(R.id.progressBarFlights)?.visibility = View.INVISIBLE
        progressBarFlights.visibility = View.INVISIBLE
    }


    private class SmoothScrollLayoutManager (val context: Context?, val speed: Float) : LinearLayoutManager(context) {
        override fun smoothScrollToPosition(
            recyclerView: RecyclerView,
            state: RecyclerView.State,
            position: Int
        ) {
            val smoothScroller: LinearSmoothScroller = object : LinearSmoothScroller(context) {
                override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                    return speed / displayMetrics.densityDpi
                }
            }
            smoothScroller.targetPosition = position
            startSmoothScroll(smoothScroller)
        }
    }
}