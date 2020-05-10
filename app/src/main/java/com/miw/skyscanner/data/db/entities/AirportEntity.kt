package com.miw.skyscanner.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

object AirportTable {
    const val TABLE_NAME = "Airport"
    const val AIRPORT_CODE = "airportCode"
    const val NAME = "name"
    const val CITY = "city"
    const val COUNTRY = "country"
    const val PHONE = "phone"
    const val POSTAL_CODE = "postalCode"
    const val LATITUDE = "latitude"
    const val LONGITUDE = "longitude"
}

@Entity(tableName = AirportTable.TABLE_NAME)
data class AirportEntity(
    @PrimaryKey @ColumnInfo(name = AirportTable.AIRPORT_CODE) @NotNull val airportCode: String,
    @ColumnInfo(name = AirportTable.NAME) @NotNull val name: String,
    @ColumnInfo(name = AirportTable.CITY) @NotNull val city: String,
    @ColumnInfo(name = AirportTable.COUNTRY) @NotNull val country: String,
    @ColumnInfo(name = AirportTable.PHONE) @NotNull val phone: String,
    @ColumnInfo(name = AirportTable.POSTAL_CODE) @NotNull val postalCode: String,
    @ColumnInfo(name = AirportTable.LATITUDE) @NotNull val latitude: Double,
    @ColumnInfo(name = AirportTable.LONGITUDE) @NotNull val longitude: Double
) {
    @Ignore
    var forecasts: List<ForecastEntity> = emptyList()
}