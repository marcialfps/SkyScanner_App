package com.miw.skyscanner.data.db.daos

import androidx.room.*
import com.miw.skyscanner.data.db.entities.ForecastEntity
import com.miw.skyscanner.data.db.entities.ForecastTable

@Dao
interface ForecastDao {
    @Insert
    fun insert(airport: ForecastEntity): Long

    @Update
    fun update(vararg forecast: ForecastEntity)

    @Delete
    fun delete(vararg  forecast: ForecastEntity)

    @Query("DELETE FROM ${ForecastTable.TABLE_NAME}")
    fun clear()

    @Query(
        "SELECT *" +
                " FROM ${ForecastTable.TABLE_NAME}" +
                " WHERE ${ForecastTable.AIRPORT_ID} LIKE :airportCode" +
                " AND ${ForecastTable.TIME} >= :date"
    )
    fun getForecastByAirportCodeAndDate(airportCode: String, date: Long): List<ForecastEntity>
}