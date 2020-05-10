package com.miw.skyscanner.data.db.daos

import androidx.room.*
import com.miw.skyscanner.data.db.entities.PlaneEntity
import com.miw.skyscanner.data.db.entities.PlaneTable

@Dao
interface PlaneDao {
    @Insert
    fun insert(plane: PlaneEntity): Long

    @Update
    fun update(vararg plane: PlaneEntity)

    @Delete
    fun delete(vararg  plane: PlaneEntity)

    @Query("DELETE FROM ${PlaneTable.TABLE_NAME}")
    fun clear()


    @Query(
        "SELECT *" +
                " FROM ${PlaneTable.TABLE_NAME}" +
                " WHERE ${PlaneTable.ICAO24_CODE} LIKE :icao24"
    )
    fun getPlaneByIcao24(icao24: String): PlaneEntity


    @Query(
        "SELECT *" +
                " FROM ${PlaneTable.TABLE_NAME}" +
                " WHERE ${PlaneTable.DEPARTURE_AIRPORT_CODE} LIKE :airportCode"
    )
    fun getPlanesByDepartureAirportCode(airportCode: String): List<PlaneEntity>

    @Query(
        "SELECT *" +
                " FROM ${PlaneTable.TABLE_NAME}" +
                " WHERE ${PlaneTable.ARRIVAL_AIRPORT_CODE} LIKE :airportCode"
    )
    fun getPlanesByArrivalAirportCode(airportCode: String): List<PlaneEntity>

    @Query(
        "DELETE" +
                " FROM ${PlaneTable.TABLE_NAME}" +
                " WHERE ${PlaneTable.ARRIVAL_AIRPORT_CODE} LIKE :airportCode"
    )
    fun removeAllPlanesByArrivalCode(airportCode: String)

    @Query(
        "DELETE" +
                " FROM ${PlaneTable.TABLE_NAME}" +
                " WHERE ${PlaneTable.DEPARTURE_AIRPORT_CODE} LIKE :airportCode"
    )
    fun removeAllPlanesByDepartureCode(airportCode: String)
}