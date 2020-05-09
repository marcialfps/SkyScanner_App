package com.miw.skyscanner.ui.map

import android.os.AsyncTask
import com.miw.skyscanner.ui.map.MapFragment.MapPlane
import com.miw.skyscanner.ws.CallWebService

class UpdateMapTask(private val mapFragment: MapFragment) : AsyncTask<Void, Void, List<MapPlane>>() {

    private val webService = CallWebService()

    override fun doInBackground(vararg params: Void?): List<MapPlane> {
        return try {
            webService.callGetPlanesClose(EXAMPLE_AIRPORT_CODE).filter {
                it.planeStatus?.location != null
            }.map { MapPlane(mapFragment.context, it) }

        } catch (e: Exception){
            emptyList()
        }
    }

    override fun onPreExecute() {
        super.onPreExecute()
        mapFragment.dataNeedsRefresh = false
    }

    override fun onPostExecute(result: List<MapPlane>?) {
        super.onPostExecute(result)
        if (result != null)
            mapFragment.planesOnMap = result

        mapFragment.dataNeedsRefresh = true
    }
}