package com.miw.skyscanner.ui.home.flights

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.miw.skyscanner.R
import com.miw.skyscanner.model.Plane
import com.miw.skyscanner.ui.flights.list.FetchPlanesTask
import com.miw.skyscanner.utils.ConversionHelper

class HomeDeparturesFragment(override var isRefreshing: Boolean = false) : Fragment(), HomeFlightsFragment {

    override val isArrivals = false
    private lateinit var columnNamesPrefix: String
    private lateinit var columnDatePrefix: String
    private lateinit var columnHoursPrefix: String
    override var task: FetchPlanesTask? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_departures, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        columnNamesPrefix = resources.getString(R.string.departures_table_prefix_name)
        columnDatePrefix = resources.getString(R.string.departures_table_prefix_date)
        columnHoursPrefix = resources.getString(R.string.departures_table_prefix_time)
        updateFlights()
    }

    override fun onStop() {
        super.onStop()
        task?.cancel(true)
    }

    // Add data programmatically
    override fun fillTableRows (flights: List<Plane>) {
        flights.forEachIndexed { index, plane ->
            val nameCellId = context?.resources?.getIdentifier(
                "$columnNamesPrefix$index", "id", activity?.packageName)

            val dateCellId = context?.resources?.getIdentifier(
                "$columnDatePrefix$index", "id", context?.packageName)

            val timeCellId = context?.resources?.getIdentifier(
                "$columnHoursPrefix$index", "id", activity?.packageName)

            if (nameCellId != null && dateCellId != null && timeCellId != null) {
                view?.findViewById<TextView>(nameCellId)?.text = plane.arrivalAirportCode
                view?.findViewById<TextView>(timeCellId)?.text =
                    plane.departureTime?.let { ConversionHelper.formatDateTimeToHour(it) }


                val dateText = view?.findViewById<TextView>(dateCellId)
                if (ConversionHelper.isDateToday(plane.departureTime!!)) {
//                    dateText?.text = resources.getString(R.string.flights_today)
                    dateText?.setTextColor(resources.getColor(R.color.colorPrimary, null))
                }
                else {
                    dateText?.text = plane.departureTime?.let { ConversionHelper.formatDateTimeToDate(it) }
                    dateText?.setTextColor(resources.getColor(R.color.secondaryTextLight, null))
                }
            }
        }
    }
}