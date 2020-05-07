package com.miw.skyscanner.model

class Forecast {
    var time: Long = 0
    lateinit var main: String
    lateinit var description: String
    var temperature: Float = 0F
    var temperatureMax: Float = 0F
    var temperatureMin: Float = 0F
    var pressure: Int = 0
    var humidity: Int = 0
    var windSpeed: Float = 0F
    var windDirection: Float = 0F
    var cloudiness: Int = 0
}