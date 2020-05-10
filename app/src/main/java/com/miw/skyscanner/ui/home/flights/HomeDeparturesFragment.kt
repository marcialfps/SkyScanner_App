package com.miw.skyscanner.ui.home.flights

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.miw.skyscanner.R
import com.miw.skyscanner.model.Plane
import com.miw.skyscanner.utils.ConversionHelper

class HomeDeparturesFragment(override var isRefreshing: Boolean = false) : Fragment(), HomeFlightsFragment {

    private val columnNamesPrefix = context?.getString(R.string.departures_table_prefix_name)
    private val columnHoursPrefix = context?.getString(R.string.departures_table_prefix_time)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_departures, container, false)
    }

    // Add data programmatically
    override fun fillTableRows (flights: List<Plane>) {
        flights.forEachIndexed { index, plane ->
            val nameCellId = context?.resources?.getIdentifier(
                "$columnNamesPrefix$index", "id", context?.packageName)

            val timeCellId = context?.resources?.getIdentifier(
                "$columnHoursPrefix$index", "id", context?.packageName)

            if (nameCellId != null && timeCellId != null) {
                view?.findViewById<TextView>(nameCellId)?.text = plane.departureAirportCode
                view?.findViewById<TextView>(timeCellId)?.text =
                    plane.arrivalTime?.let { ConversionHelper.formatDateTimeToHour(it) }
            }
        }
    }
}