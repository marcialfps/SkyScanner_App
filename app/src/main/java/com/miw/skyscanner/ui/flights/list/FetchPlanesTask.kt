package com.miw.skyscanner.ui.flights.list

import android.os.AsyncTask
import androidx.fragment.app.Fragment
import com.miw.skyscanner.model.Plane
import com.miw.skyscanner.ui.home.flights.HomeFlightsFragment
import com.miw.skyscanner.ui.home.flights.NUMBER_OF_FLIGHTS_IN_HOME
import com.miw.skyscanner.ui.map.EXAMPLE_AIRPORT_CODE
import com.miw.skyscanner.utils.PlaneComparator
import com.miw.skyscanner.ws.CallWebService
import java.time.LocalDateTime

class FetchPlanesTask(private var parentFragment: Fragment) :
    AsyncTask<Boolean, Void, List<Plane>>() {

    private var parentIsHomeScreen: Boolean = false
    private val webService = CallWebService()
    private var planesLimit = 0
    private val currentTime = LocalDateTime.now()

    override fun doInBackground(vararg params: Boolean?): List<Plane> {

        if (!parentIsHomeScreen) {
            with(parentFragment as FlightsListFragment) {
                if (this.isRefreshing) return emptyList()
                this.isRefreshing = true
            }
        }
        else {
            return emptyList()
        }

        return try {
            val isArrival = params[0]!!

            webService.callGetPlanesByAirport(isArrival, EXAMPLE_AIRPORT_CODE, planesLimit)
                // Filter incomplete flights that we will not show
                .filter {
                    it.arrivalAirportCode != null && it.departureAirportCode != null &&
                        it.arrivalTime != null && it.departureTime != null &&
                            it.arrivalDistance != null && it.departureDistance != null
                }
                // The API returns flights 2 days old. Alter the date on purpose
                // to show flights today or tomorrow and be more realistic
                .map {
                    if (isArrival) it.arrivalTime = it.arrivalTime?.plusDays((1..2).random().toLong())
                    else it.departureTime = it.departureTime?.plusDays((1..2).random().toLong())
                    it
                }
                // Show only present and future flights
                .filter {
                    if (isArrival) it.arrivalTime!!.isAfter(currentTime)
                    else it.departureTime!!.isAfter(currentTime)
                }
                // Sort flights by date
                .sortedWith(PlaneComparator(isArrival))

        } catch (e: Exception){
            onPostExecute(null)
            emptyList()
        }
    }

    override fun onPostExecute(result: List<Plane>?) {
        super.onPostExecute(result)
        if (result != null) {
            if (!parentIsHomeScreen) {
                with(parentFragment as FlightsListFragment) {
                    this.flights = result
                    this.parent.currentFragment()?.isRefreshing = false
                }
            }
            else {
                with(parentFragment as HomeFlightsFragment) {
                    this.fillTableRows(result)
                }
            }
        }
    }

    override fun onPreExecute() {
        super.onPreExecute()
        parentIsHomeScreen = parentFragment !is FlightsListFragment
        if (parentIsHomeScreen) planesLimit = NUMBER_OF_FLIGHTS_IN_HOME
    }


}