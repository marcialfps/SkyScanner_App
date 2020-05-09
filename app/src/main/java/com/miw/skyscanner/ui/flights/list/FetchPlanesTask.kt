package com.miw.skyscanner.ui.flights.list

import android.os.AsyncTask
import com.miw.skyscanner.model.Plane
import com.miw.skyscanner.ui.map.EXAMPLE_AIRPORT_CODE
import com.miw.skyscanner.ui.map.MapFragment.MapPlane
import com.miw.skyscanner.ws.CallWebService

class FetchPlanesTask(private val flightsListFragment: FlightsListFragment) :
    AsyncTask<Boolean, Void, List<Plane>>() {

    private val webService = CallWebService()
    override fun doInBackground(vararg params: Boolean?): List<Plane> {
        return try {
            // Use the thread to also filter incomplete flights that we will not show
            webService.callGetPlanesByAirport(params[0]!!, EXAMPLE_AIRPORT_CODE).filter {
                it.arrivalAirportCode != null && it.departureAirportCode != null &&
                        it.arrivalTime != null && it.departureTime != null &&
                                it.arrivalDistance != null && it.departureDistance != null
            }
        } catch (e: Exception){
            emptyList()
        }
    }

    override fun onPostExecute(result: List<Plane>?) {
        super.onPostExecute(result)
        if (result != null)
            flightsListFragment.planes = result
    }


}