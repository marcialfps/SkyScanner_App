package com.miw.skyscanner.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

object ForecastTable {
    const val TABLE_NAME = "Forecast"
    const val ID = "id"
    const val TIME = "time"
    const val MAIN = "main"
    const val DESCRIPTION = "description"
    const val TEMPERATURE = "temperature"
    const val TEMPERATURE_MAX = "temperatureMax"
    const val TEMPERATURE_MIN = "temperatureMin"
    const val PRESSURE = "pressure"
    const val HUMIDITY = "humidity"
    const val WIND_SPEED = "windSpeed"
    const val WIND_DIRECTION = "windDirection"
    const val CLOUDINESS = "cloudiness"
    const val AIRPORT_ID = "airportId"
}

@Entity(
    tableName = ForecastTable.TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = AirportEntity::class,
        parentColumns = [AirportTable.AIRPORT_CODE],
        childColumns = [ForecastTable.AIRPORT_ID],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ForecastEntity(
    @ColumnInfo(name = ForecastTable.TIME) @NotNull val time: Long,
    @ColumnInfo(name = ForecastTable.MAIN) @NotNull val main: String,
    @ColumnInfo(name = ForecastTable.DESCRIPTION) @NotNull val description: String,
    @ColumnInfo(name = ForecastTable.TEMPERATURE) @NotNull val temperature: Double,
    @ColumnInfo(name = ForecastTable.TEMPERATURE_MAX) @NotNull val temperatureMax: Double,
    @ColumnInfo(name = ForecastTable.TEMPERATURE_MIN) @NotNull val temperatureMin: Double,
    @ColumnInfo(name = ForecastTable.PRESSURE) @NotNull val pressure: Int,
    @ColumnInfo(name = ForecastTable.HUMIDITY) @NotNull val humidity: Int,
    @ColumnInfo(name = ForecastTable.WIND_SPEED) @NotNull val windSpeed: Double,
    @ColumnInfo(name = ForecastTable.WIND_DIRECTION) @NotNull val windDirection: Double,
    @ColumnInfo(name = ForecastTable.CLOUDINESS) @NotNull val cloudiness: Int
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ForecastTable.ID)
    var id: Long = 0

    @ColumnInfo(name = ForecastTable.AIRPORT_ID)
    @NotNull
    var airportId: Long = 0
}