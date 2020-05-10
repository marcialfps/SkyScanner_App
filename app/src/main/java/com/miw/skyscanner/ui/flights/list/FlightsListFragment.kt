package com.miw.skyscanner.ui.flights.list

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
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
                           val parent: FlightsCollectionAdapter) : Fragment() {

    var isRefreshing = false

    private var previousFlights: List<Plane> = emptyList()
    // List of items that the view must handle
    var flights: List<Plane> by Delegates.observable(listOf()) {
            _, oldList, newList ->
        parent.parent.onRefreshEnd()
        if (oldList.isNotEmpty()) previousFlights = oldList
        if (newList.isEmpty()) notifyError(previousFlights.isNotEmpty())
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
        fetchFlights()
    }

    fun fetchFlights () {
        if (previousFlights.isNotEmpty())
            view?.findViewById<ProgressBar>(R.id.progressBarFlights)?.visibility = View.VISIBLE
        if (flightsListRecyclerView != null)
            FetchPlanesTask(this).execute(isArrivals)
    }

    private fun showFlightList () {
        view?.findViewById<ProgressBar>(R.id.progressBarFlights)?.visibility = View.INVISIBLE
        flightsListRecyclerView?.adapter = FlightsListAdapter(activity!!, flights, isArrivals)
    }

    private fun notifyError (makeToast: Boolean) {
        if (!makeToast)
            view?.findViewById<TextView>(R.id.txFlightsError)?.visibility = View.VISIBLE
        else
            view?.findViewById<TextView>(R.id.txFlightsError)?.visibility = View.INVISIBLE
        view?.findViewById<ProgressBar>(R.id.progressBarFlights)?.visibility = View.INVISIBLE

        if (makeToast)
            Toast.makeText(context, getString(R.string.flights_error), Toast.LENGTH_SHORT).show()

        // Set an adapter to the list but without any item
        if (previousFlights.isEmpty()) {
            Toast.makeText(context, getString(R.string.flights_error), Toast.LENGTH_SHORT).show()
            flightsListRecyclerView?.adapter = FlightsListAdapter(activity!!, emptyList(), isArrivals)
        }
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