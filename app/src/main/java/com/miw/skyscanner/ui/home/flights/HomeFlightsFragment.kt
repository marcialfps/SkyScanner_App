package com.miw.skyscanner.ui.home.flights

import androidx.fragment.app.Fragment
import com.miw.skyscanner.model.Plane
import com.miw.skyscanner.ui.flights.list.FetchPlanesTask

const val NUMBER_OF_FLIGHTS_IN_HOME = 4
interface HomeFlightsFragment {

    var isRefreshing: Boolean
    val isArrivals: Boolean
    var task: FetchPlanesTask?

    fun updateFlights () {
        if (this is Fragment) {
            task = FetchPlanesTask(this)
            task!!.execute(isArrivals)
        }
    }
    fun fillTableRows (flights: List<Plane>)
}