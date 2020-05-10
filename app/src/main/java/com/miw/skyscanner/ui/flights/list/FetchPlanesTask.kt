package com.miw.skyscanner.ui.flights.list

import android.content.Context
import android.os.AsyncTask
import com.miw.skyscanner.model.Plane
import com.miw.skyscanner.ui.map.EXAMPLE_AIRPORT_CODE
import com.miw.skyscanner.utils.PlaneComparator
import com.miw.skyscanner.utils.Session
import com.miw.skyscanner.ws.CallWebService

class FetchPlanesTask(private val flightsListFragment: FlightsListFragment) :
    AsyncTask<Boolean, Void, List<Plane>>() {

    private val webService = CallWebService()
    override fun doInBackground(vararg params: Boolean?): List<Plane> {

        if (flightsListFragment.isRefreshing) return emptyList()
        flightsListFragment.isRefreshing = true
        return try {
            val isArrival = params[0]!!
            // Use the thread to also filter incomplete flights that we will not show
            // and sort them by date
            webService.callGetPlanesByAirport(isArrival, Session(flightsListFragment.context!!).airport)
                .filter {
                it.arrivalAirportCode != null && it.departureAirportCode != null &&
                        it.arrivalTime != null && it.departureTime != null &&
                                it.arrivalDistance != null && it.departureDistance != null
                }
                // The API returns flights 2 days old. Alter the date on purpose
                // to show flights today or tomorrow and be more realistic
                .map {
                    if (isArrival) it.departureTime = it.departureTime?.plusDays((2..3).random().toLong())
                    else it.arrivalTime = it.arrivalTime?.plusDays((2..3).random().toLong())
                    it
                }
                .sortedWith(PlaneComparator(isArrival))

        } catch (e: Exception){
            onPostExecute(null)
            emptyList()
        }
    }

    override fun onPostExecute(result: List<Plane>?) {
        super.onPostExecute(result)
        if (result != null)
            flightsListFragment.flights = result
        flightsListFragment.parent.currentFragment()?.isRefreshing = false
    }


}