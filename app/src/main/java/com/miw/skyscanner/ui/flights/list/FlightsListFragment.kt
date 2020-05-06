package com.miw.skyscanner.ui.flights.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
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
        flightsArrivalsRecyclerView.layoutManager = LinearLayoutManager(activity)
        flightsArrivalsRecyclerView.adapter = FlightsListAdapter(activity!!, items, isArrivals)
    }
}