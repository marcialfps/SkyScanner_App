package com.miw.skyscanner.model

class Forecast {
    var time: Long = 0
    lateinit var main: String
    lateinit var description: String
    var temperature: Double = 0.0
    var temperatureMax: Double = 0.0
    var temperatureMin: Double = 0.0
    var pressure: Int = 0
    var humidity: Int = 0
    var windSpeed: Double = 0.0
    var windDirection: Double = 0.0
    var cloudiness: Int = 0
}