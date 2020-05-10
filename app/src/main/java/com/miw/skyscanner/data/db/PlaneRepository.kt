package com.miw.skyscanner.data.db

import com.miw.skyscanner.data.datasources.PlaneDataSource
import com.miw.skyscanner.data.db.daos.PlaneDao
import com.miw.skyscanner.model.Plane

class PlaneRepository: PlaneDataSource {
    private val planeDao: PlaneDao? = ForecastDb.instance.planeDao()

    override fun requestPlanesByAirportCode(airportCode: String, isArrivals:Boolean): List<Plane>? {
        val planeEntities =
            if (isArrivals) planeDao?.getPlanesByArrivalAirportCode(airportCode)
            else planeDao?.getPlanesByDepartureAirportCode(airportCode)

        if (planeEntities != null) {
            return planeEntities.map{ DbDataMapper.convertToDomain(it) }
        }
        return null
    }

    fun savePlane(plane: Plane) {
        if (planeDao != null) {
            planeDao.clear()
            val planeEntity = DbDataMapper.convertPlaneFromDomain(plane)
            planeDao.insert(planeEntity)
        }
    }
}