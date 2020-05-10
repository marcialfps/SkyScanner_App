package com.miw.skyscanner.data.db.daos

import androidx.room.*
import com.miw.skyscanner.data.db.entities.AirportEntity
import com.miw.skyscanner.data.db.entities.AirportTable

@Dao
interface AirportDao {
    @Insert
    fun insert(airport: AirportEntity): Long

    @Update
    fun update(vararg airport: AirportEntity)

    @Delete
    fun delete(vararg  airport: AirportEntity)

    @Query("DELETE FROM ${AirportTable.TABLE_NAME}")
    fun clear()

    @Query(
        "SELECT *" +
                " FROM ${AirportTable.TABLE_NAME}" +
                " WHERE ${AirportTable.AIRPORT_CODE} LIKE :airportCode" +
                " LIMIT 1"
    )
    fun getAirportByAirportCode(airportCode: String): AirportEntity
}