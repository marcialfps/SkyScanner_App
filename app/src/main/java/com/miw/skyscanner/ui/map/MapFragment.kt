package com.miw.skyscanner.ui.map

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.miw.skyscanner.R
import com.miw.skyscanner.model.Coordinate
import com.miw.skyscanner.model.Plane
import com.miw.skyscanner.model.PlaneStatus
import com.miw.skyscanner.ws.CallWebService
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.coroutines.*
import java.lang.StringBuilder
import kotlin.reflect.KMutableProperty


const val EXAMPLE_LATITUDE: Double = 40.4927751
const val EXAMPLE_LONGITUDE: Double = -3.5933761
const val EXAMPLE_AIRPORT_CODE: String = "KJFK"
const val ZOOM_LEVEL: Float = 8f

class MapFragment : Fragment(), OnMapReadyCallback {

    private val webService = CallWebService()
    private lateinit var googleMap: GoogleMap
    private var planesOnMap: List<MapPlane> = listOf()
    private var markersOnMap: MutableList<Marker> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mapView.onCreate(savedInstanceState)
        mapView.onResume() // Display the map immediately

        mapView.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        if (map != null) {
            googleMap = map
            // Initial camera and zoom //TODO should be focused on the logged user airport
            map.moveCamera(CameraUpdateFactory.newLatLngZoom
                (LatLng(EXAMPLE_LATITUDE, EXAMPLE_LONGITUDE), ZOOM_LEVEL))
            googleMap.setOnMapLoadedCallback { updateMap() }
        }
    }

    private fun updateMap () {
        // Fetching info toast
        Toast.makeText(activity, getString(R.string.map_loading), Toast.LENGTH_LONG).show()
        runBlocking {
            withContext(Dispatchers.IO){
                planesOnMap = fetchPlanes().map { MapPlane(context, it) }
            }
        }
        // Error info toast
        if (planesOnMap.isEmpty())
            Toast.makeText(activity, getString(R.string.map_loading_error), Toast.LENGTH_LONG).show()

        else updateMarkers()

    }

    private fun updateMarkers() {
        // Clear all markers
        markersOnMap.forEach { it.remove() }
        markersOnMap.clear()
        // Place all new markers adn add them to the list for future removal
        planesOnMap.forEach {
            val marker: Marker = googleMap.addMarker(
                MarkerOptions().position(Coordinate(it.status?.location?.latitude,
                    it.status?.location?.longitude).getLatLng())
                    .title(it.markerTitle()).snippet(it.markerDescription())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.plane_marker))
            )
            markersOnMap.add(marker)
        }
    }

    private fun fetchPlanes(): List<Plane> {
        // Fetch planes nearby and filter the ones with location not set to null
        return try {
            val a = webService.callGetAirportByCode("LEMD")
            Log.e("AIRPORT", a.toString())
            webService.callGetPlanesClose(EXAMPLE_AIRPORT_CODE).filter {
                it.planeStatus?.location != null
            }
        } catch (e: Exception){
            emptyList()
        }
    }

    class MapPlane (val context: Context?, private val plane: Plane) {

        val status = plane.planeStatus

        val markerTitle: () -> String? = {
            if (plane.planeStatus == null) context?.getString(R.string.map_aircraft_unknown)
            else context?.getString(R.string.map_aircraft, plane.planeStatus?.icao24)
        }

        // TODO
        val markerDescription: () -> String? = {
//            val sb = StringBuilder()
//
//            for (prop in PlaneStatus::class.members.filterIsInstance<KMutableProperty<*>>()) {
//                println("${prop.name} = ${prop.}")
//            }
//
//            sb.toString()
            ""

        }
    }
}