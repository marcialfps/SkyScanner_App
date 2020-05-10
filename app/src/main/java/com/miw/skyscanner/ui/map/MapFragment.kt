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
import java.util.*
import kotlin.concurrent.fixedRateTimer
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.properties.Delegates


const val EXAMPLE_LATITUDE: Double = 40.6413111
const val EXAMPLE_LONGITUDE: Double = -73.7781391
const val EXAMPLE_AIRPORT_CODE: String = "KJFK"
const val ZOOM_LEVEL: Float = 8f
const val POLYLINE_STROKE_WIDTH_PX = 12f
const val TIME_BETWEEN_REQUESTS:Long = 4500

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private var previousPlanesOnMap: List<MapPlane> = emptyList()
    var planesOnMap: List<MapPlane> by Delegates.observable(listOf()) {
        _, oldList, newList ->
        if (newList.isEmpty()) notifyError()
        else {
            if (oldList.isNotEmpty())
                previousPlanesOnMap = oldList
            updateTrailsAndRotation (previousPlanesOnMap)
            updateMarkers()
        }
    }
    private var markersOnMap: MutableList<Marker> = mutableListOf()
    var dataNeedsRefresh: Boolean = true
    private var interval: Timer? = null

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

    override fun onStop() {
        super.onStop()
        interval?.cancel()
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
            googleMap.setOnMapLoadedCallback {startUpdateInterval()}
        }
    }

    private fun startUpdateInterval () {
        interval = fixedRateTimer("refreshTimer", true, 0, TIME_BETWEEN_REQUESTS){
            if (dataNeedsRefresh) UpdateMapTask(this@MapFragment).execute()
        }
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
                    .rotation(it.rotation)
                    .title(it.markerTitle()).snippet(it.markerDescription())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.plane_marker_2)                    )
            )
            markersOnMap.add(marker)
        }
    }

    private fun updateTrailsAndRotation(oldPositions: List<MapPlane>) {
        // Planes that were present in the last iteration and now, so must be updated
        val updatedPlanes: Set<MapPlane> = oldPositions.intersect(planesOnMap)
        // Planes that were present in the last iteration but not now, and must be removed
        val missingPlanes: Set<MapPlane> = oldPositions.toSet() - updatedPlanes
        updateTrails(oldPositions, updatedPlanes, missingPlanes)
        updateRotations(oldPositions, updatedPlanes)

    }

    private fun updateTrails(oldPositions: List<MapPlane>, updatedPlanes: Set<MapPlane>,
                             missingPlanes: Set<MapPlane>) {

        // Purge old trails
        missingPlanes.forEach {it.clearTrail()}

        // Create new trails
        updatedPlanes.forEach {plane ->

            val outdatedPlane: MapPlane? = oldPositions.find {
                it.status?.icao24 == plane.status?.icao24 }

            val updatedPlane:MapPlane? = planesOnMap.find {
                it.status?.icao24 == plane.status?.icao24 }


            if (outdatedPlane != null && updatedPlane != null) {
                updatedPlane.trail = outdatedPlane.trail
                val trail = googleMap.addPolyline(
                    PolylineOptions()
                        .clickable(false)
                        .visible(true)
                        .width(POLYLINE_STROKE_WIDTH_PX)
                        .color(R.color.colorPrimary)
                        .jointType(JointType.ROUND)
                        .endCap(RoundCap())
                        .add(
                            outdatedPlane.status?.location?.getLatLng(),
                            updatedPlane.status?.location?.getLatLng()
                        )
                )
                updatedPlane.trail.add(trail)
            }
        }
    }

    private fun updateRotations (oldPositions: List<MapPlane>, updatedPlanes: Set<MapPlane>) {
        updatedPlanes.forEach {plane ->

            // New and old positions
            val updatedPlane: MapPlane? = planesOnMap.find {
                it.status?.icao24 == plane.status?.icao24 }

            val outdatedPlane: MapPlane? = oldPositions.find {
                it.status?.icao24 == plane.status?.icao24 }

            // If we have enough info, calculate the rotation
            if (updatedPlane?.status?.location != null && outdatedPlane?.status?.location != null){
                val deltaX =
                    updatedPlane.status.location!!.longitude!! - outdatedPlane.status.location!!.longitude!!
                val deltaY =
                    updatedPlane.status.location!!.latitude!! - outdatedPlane.status.location!!.latitude!!

                if (deltaX == 0.0 && deltaY == 0.0)
                    updatedPlane.rotation = outdatedPlane.rotation
                else
                    updatedPlane.rotation = (atan2(deltaX, deltaY) * 180 / PI).toFloat()
            }

        }
    }

    private fun notifyError () {
        // Error info toast
        if (planesOnMap.isEmpty() && context != null) {
            Toast.makeText(activity, getString(R.string.map_loading_error), Toast.LENGTH_LONG).show()
        }
    }

    class MapPlane (val context: Context?, plane: Plane) {

        val status = plane.planeStatus
        var rotation: Float = 0f
        var trail: MutableList<Polyline> = mutableListOf()

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

        fun clearTrail () {
            trail.forEach { it.remove()}
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
            return status?.icao24.hashCode()
        }

    }
}