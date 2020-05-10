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
}

@Entity(tableName = AirportTable.TABLE_NAME)
data class AirportEntity(
    @PrimaryKey @ColumnInfo(name = AirportTable.AIRPORT_CODE) @NotNull val airportCode: String,
    @ColumnInfo(name = AirportTable.NAME) @NotNull val name: String
) {
    @Ignore
    var forecasts: List<ForecastEntity> = emptyList()
}