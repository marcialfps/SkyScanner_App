package com.miw.skyscanner.model

class PlaneStatus (var icao24: String? = null, var lastUpdate: Int? = null, var location: Coordinate? = null,
                   var altitude: Double? = null, var speed: Double? = null, var onGround:Boolean? = null,
                   var verticalRate: Double? = null, var ascending: Boolean? = null) {

    override fun toString(): String {
        return "PlaneStatus(icao24='$icao24', lastUpdate=$lastUpdate, " +
                "location=$location, altitude=$altitude, " +
                "speed=$speed, onGround=$onGround, " +
                "verticalRate=$verticalRate, ascending=$ascending)"
    }
}