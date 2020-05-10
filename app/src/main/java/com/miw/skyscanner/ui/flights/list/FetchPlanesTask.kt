package com.miw.skyscanner.ui.flights.list

import android.os.AsyncTask
import androidx.fragment.app.Fragment
import com.miw.skyscanner.data.datasources.DataProvider
import com.miw.skyscanner.model.Plane
import com.miw.skyscanner.ui.home.flights.HomeFlightsFragment
import com.miw.skyscanner.ui.home.flights.NUMBER_OF_FLIGHTS_IN_HOME
import com.miw.skyscanner.utils.PlaneComparator
import com.miw.skyscanner.utils.Session
import com.miw.skyscanner.ws.CallWebService
import java.time.LocalDateTime

class FetchPlanesTask(private var parentFragment: Fragment,
                      private val forceQueryServer: Boolean = false) :
    AsyncTask<Boolean, Void, List<Plane>>() {

    private var parentIsHomeScreen: Boolean = false
    private var planesLimit = 0

    override fun doInBackground(vararg params: Boolean?): List<Plane> {

        if (!parentIsHomeScreen) {
            with(parentFragment as FlightsListFragment) {
                if (this.isRefreshing) return emptyList()
                else this.isRefreshing = true
            }
        }
        else {
            with(parentFragment as HomeFlightsFragment) {
                if (this.isRefreshing) return emptyList()
                else this.isRefreshing = true
            }
        }

        return try {
            val isArrival = params[0]!!
            var planes = DataProvider.requestPlanesByAirportCode(
                Session(parentFragment.context!!).airport, isArrival, forceQueryServer) ?: emptyList()

            if (planes.isNotEmpty() && planesLimit > 0 && planesLimit < planes.size)
                planes = planes.subList(0, planesLimit)

            planes

        } catch (e: Exception){
            throw e
            emptyList()
        }
    }

    override fun onPostExecute(result: List<Plane>?) {
        super.onPostExecute(result)
        if (result != null) {
            if (!parentIsHomeScreen) {
                with(parentFragment as FlightsListFragment) {
                    this.flights = result
                    this.isRefreshing = false
                }
            }
            else {
                with(parentFragment as HomeFlightsFragment) {
                    this.fillTableRows(result)
                    this.isRefreshing = false
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