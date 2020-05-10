package com.miw.skyscanner.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

object PlaneTable {
    const val TABLE_NAME = "Plane"
    const val ICAO24_CODE = "icao24"
    const val DEPARTURE_AIRPORT_CODE = "departureAirportCode"
    const val ARRIVAL_AIRPORT_CODE = "arrivalAirportCode"
    const val DEPARTURE_TIME = "departureTime"
    const val ARRIVAL_TIME = "arrivalTime"
    const val DEPARTURE_DISTANCE = "departureDistance"
    const val ARRIVAL_DISTANCE = "arrivalDistance"
}

@Entity(tableName = PlaneTable.TABLE_NAME)
data class PlaneEntity(
    @PrimaryKey @ColumnInfo(name = PlaneTable.ICAO24_CODE) @NotNull val icao24: String,
    @ColumnInfo(name = PlaneTable.DEPARTURE_AIRPORT_CODE) @NotNull val departureAirportCode: String,
    @ColumnInfo(name = PlaneTable.ARRIVAL_AIRPORT_CODE) @NotNull val arrivalAirportCode: String,
    @ColumnInfo(name = PlaneTable.DEPARTURE_TIME) @NotNull val departureTime: Int,
    @ColumnInfo(name = PlaneTable.ARRIVAL_TIME) @NotNull val arrivalTime: Int,
    @ColumnInfo(name = PlaneTable.DEPARTURE_DISTANCE) @NotNull val departureDistance: Int,
    @ColumnInfo(name = PlaneTable.ARRIVAL_DISTANCE) @NotNull val arrivalDistance: Int
)