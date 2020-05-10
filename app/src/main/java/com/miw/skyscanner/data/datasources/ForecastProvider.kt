package com.miw.skyscanner.data.datasources

import com.miw.skyscanner.data.db.ForecastRepository
import com.miw.skyscanner.data.server.ForecastServer
import com.miw.skyscanner.model.ForecastList

object ForecastProvider {
    private const val DAYS = 3
    private val SOURCES = listOf(ForecastRepository(), ForecastServer())

    fun requestForecast(): ForecastList? {
        for (source in SOURCES) {
            val result = source.requestForecastByDate(System.currentTimeMillis())
            if (result != null && result.dailyForecast.size >= DAYS)
                return result
        }
        return null
    }
}