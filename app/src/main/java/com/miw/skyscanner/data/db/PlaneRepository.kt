package com.miw.skyscanner.data.db

import com.miw.skyscanner.data.datasources.PlaneDataSource
import com.miw.skyscanner.data.db.daos.PlaneDao
import com.miw.skyscanner.model.Plane

class PlaneRepository: PlaneDataSource {
    private val planeDao: PlaneDao? = AppDb.instance.planeDao()

    override fun requestPlanesByAirportCode(
        airportCode: String,
        isArrivals: Boolean,
        resetTable: Boolean
    ): List<Plane>? {
        val planeEntities =
            if (isArrivals) planeDao?.getPlanesByArrivalAirportCode(airportCode)
            else planeDao?.getPlanesByDepartureAirportCode(airportCode)

        if (planeEntities != null && planeEntities.isNotEmpty()) {
            return planeEntities.map{ DbDataMapper.convertToDomain(it) }
        }
        return null
    }

    private fun savePlane(plane: Plane) {
        if (planeDao != null) {
            val newPlaneEntity = DbDataMapper.convertPlaneFromDomain(plane)
            val oldPlane = planeDao.getPlaneByIcao24(newPlaneEntity.icao24)
            if (oldPlane == null) // DB can return null anyway
                planeDao.insert(newPlaneEntity)
        }
    }

    fun savePlanes (planes: List<Plane>, resetArrivals: Boolean, resetDepartures: Boolean){
        if (resetArrivals && planes.isNotEmpty())
            planeDao?.removeAllPlanesByArrivalCode(planes[0].arrivalAirportCode!!)
        if (resetDepartures && planes.isNotEmpty())
            planeDao?.removeAllPlanesByDepartureCode(planes[0].departureAirportCode!!)
        planes.forEach { savePlane(it) }
    }
}