package com.miw.skyscanner.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.miw.skyscanner.App
import com.miw.skyscanner.data.db.daos.AirportDao
import com.miw.skyscanner.data.db.daos.ForecastDao
import com.miw.skyscanner.data.db.daos.PlaneDao
import com.miw.skyscanner.data.db.entities.AirportEntity
import com.miw.skyscanner.data.db.entities.ForecastEntity
import com.miw.skyscanner.data.db.entities.PlaneEntity

@Database(entities = [AirportEntity::class, ForecastEntity::class, PlaneEntity::class], version = 1)
abstract class ForecastDb : RoomDatabase() {

    abstract fun forecastDao(): ForecastDao
    abstract fun airportDao(): AirportDao
    abstract fun planeDao(): PlaneDao

    companion object {
        private const val DATABASE_NAME = "database"
        val instance by lazy {
            Room.databaseBuilder(
                App.instance,
                ForecastDb::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}