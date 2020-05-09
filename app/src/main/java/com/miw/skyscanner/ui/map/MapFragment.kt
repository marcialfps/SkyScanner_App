package com.miw.skyscanner.ui.map

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.miw.skyscanner.R
import com.miw.skyscanner.model.Coordinate
import com.miw.skyscanner.model.Plane
import kotlinx.android.synthetic.main.fragment_map.*
import kotlin.concurrent.fixedRateTimer
import kotlin.properties.Delegates


const val EXAMPLE_LATITUDE: Double = 40.6413111
const val EXAMPLE_LONGITUDE: Double = -73.7781391
const val EXAMPLE_AIRPORT_CODE: String = "KJFK"
const val ZOOM_LEVEL: Float = 8f
const val POLYLINE_STROKE_WIDTH_PX = 12f

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    var planesOnMap: List<MapPlane> by Delegates.observable(listOf()) {
        _, oldList, newList ->
        if (newList.isEmpty()) notifyError()
        else {
            updateMarkers()
            drawTrails(oldList)
        }
    }
    private var markersOnMap: MutableList<Marker> = mutableListOf()
    var dataNeedsRefresh: Boolean = true

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
            map.uiSettings.isCompassEnabled = true
            // Fetching info toast
            Toast.makeText(activity, getString(R.string.map_loading), Toast.LENGTH_LONG).show()
            googleMap.setOnMapLoadedCallback {
                fixedRateTimer("refreshTimer", true, 0, 3000){
                    if (dataNeedsRefresh) updateMapData()
                }
            }
        }
    }

    private fun updateMapData(){
        // Update the list of planes, which is an observable that will trigger
        // the update of the map markers
        UpdateMapTask(this).execute()
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
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.plane_marker_2))
            )
            markersOnMap.add(marker)
        }
    }

    private fun drawTrails(oldPositions: List<MapPlane>) {
        val updatedPlanes = oldPositions.intersect(planesOnMap)
        updatedPlanes.forEach {plane ->

            val strokeStart:LatLng? = oldPositions.find {
                it.status?.icao24 == plane.status?.icao24 }?.status?.location?.getLatLng()

            val strokeEnd:LatLng? = planesOnMap.find {
                it.status?.icao24 == plane.status?.icao24 }?.status?.location?.getLatLng()

            if (strokeStart != null && strokeEnd != null) {
                googleMap.addPolyline(
                    PolylineOptions()
                        .clickable(false)
                        .visible(true)
                        .width(POLYLINE_STROKE_WIDTH_PX)
                        .color(R.color.colorPrimary)
                        .jointType(JointType.ROUND)
                        .endCap(RoundCap())
                        .add(
                            strokeStart, strokeEnd
                        )
                )
            }
        }


    }

    private fun notifyError () {
        // Error info toast
        if (planesOnMap.isEmpty())
            Toast.makeText(activity, getString(R.string.map_loading_error), Toast.LENGTH_LONG).show()
    }

    class MapPlane (val context: Context?, plane: Plane) {

        val status = plane.planeStatus

        val markerTitle: () -> String = {
            if (context != null) {
                if (status != null)
                    context.getString(R.string.map_aircraft, status.icao24)
                else
                    context.getString(R.string.map_aircraft_unknown)
            }
            else "Marker"
        }

        val markerDescription: () -> String? = {

            if (context != null) {
                if (status?.speed != null){
                    context.getString(R.string.map_speed, status.speed)}
                else
                    context.getString(R.string.map_unavailable)
            }
            else ""
        }

        // Equals depends on the icao code
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as MapPlane

            if (status?.icao24 != other.status?.icao24) return false

            return true
        }

        override fun hashCode(): Int {
            return status?.icao24.hashCode() ?: 0
        }

    }
}