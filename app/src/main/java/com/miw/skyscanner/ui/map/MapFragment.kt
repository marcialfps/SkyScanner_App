package com.miw.skyscanner.ui.map

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
import com.google.android.gms.maps.model.MarkerOptions
import com.miw.skyscanner.R
import com.miw.skyscanner.model.Coordinate
import com.miw.skyscanner.model.Plane
import com.miw.skyscanner.ws.CallWebService
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.coroutines.*
import java.lang.Exception


const val EXAMPLE_LATITUDE: Double = 40.4927751
const val EXAMPLE_LONGITUDE: Double = -3.5933761
const val EXAMPLE_AIRPORT_CODE: String = "LEMD"
const val ZOOM_LEVEL: Float = 8f

class MapFragment : Fragment(), OnMapReadyCallback {

    private val webService = CallWebService()
    private lateinit var googleMap: GoogleMap

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
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(EXAMPLE_LATITUDE, EXAMPLE_LONGITUDE), ZOOM_LEVEL))
            // Fetching info toast
            Toast.makeText(activity, getString(R.string.map_loading), Toast.LENGTH_LONG).show()
            runBlocking {
                withContext(Dispatchers.IO){
                    updateMap()
                }
            }
        }
    }

    private fun updateMap () {

        var planes: List<Plane> = emptyList()
        runBlocking {
            withContext(Dispatchers.IO){
                planes = getPlanesClose()
            }
        }

        Log.e("PLANES: ", planes.toString())
//        try {
//            getPlanesClose()
//        }
//        catch (e: Exception){
//            Toast.makeText(activity, getString(R.string.map_loading_error), Toast.LENGTH_LONG).show()
//        }

//        googleMap.addMarker(
//            MarkerOptions().position(Coordinate(EXAMPLE_LATITUDE, EXAMPLE_LONGITUDE).getLatLng())
//                .title("Marker in Spain")
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.plane_marker))
//        )
    }

    private suspend fun getPlanesClose (): List<Plane> {
//        val planes = CoroutineScope(Dispatchers.IO).async {
//            webService.callPlanesCloseToAirport(EXAMPLE_AIRPORT_CODE)
//        }
//        return try {
//            planes.await()
//        } catch (e: Exception){
//            throw e
//            emptyList()
//        }
        return emptyList<Plane>()
    }
}