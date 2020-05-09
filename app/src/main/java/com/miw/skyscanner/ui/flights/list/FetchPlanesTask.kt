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
            webService.callGetPlanesByAirport(params[0]!!, EXAMPLE_AIRPORT_CODE)
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