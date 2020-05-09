package com.miw.skyscanner.model

class Airport (
    var code: String? = null, var name: String? = null, var city: String? = null, var country: String? = null,
    var phone: String? = null, var postalCode: String? = null, var location: Coordinate? = null,
    var forecast: String? = null) // TODO forecast should be Forecast type