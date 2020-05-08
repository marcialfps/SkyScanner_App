package com.miw.skyscanner.ui.flights.list

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.miw.skyscanner.R
import kotlinx.android.synthetic.main.fragment_flights_list.*


class FlightsListFragment (private val isArrivals: Boolean) : Fragment() {

    // List of items that the view handle
    // TODO should be Flight data
    private val items = listOf(
        "Item",
        "Item",
        "Item",
        "Item",
        "Item",
        "Item",
        "Item",
        "Item",
        "Item",
        "Item"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_flights_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        flightsListRecyclerView.layoutManager = SmoothScrollLayoutManager(context, 60f)
        flightsListRecyclerView.adapter = FlightsListAdapter(activity!!, items, isArrivals)
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