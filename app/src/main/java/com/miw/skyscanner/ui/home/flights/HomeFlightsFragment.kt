package com.miw.skyscanner.ui.home.flights

import com.miw.skyscanner.model.Plane

const val NUMBER_OF_FLIGHTS_IN_HOME = 4
interface HomeFlightsFragment {

    val isRefreshing: Boolean

    fun fillTableRows (flights: List<Plane>)
}