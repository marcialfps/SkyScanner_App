package com.miw.skyscanner.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

// Additional methods to help when building model entities from SOAP responses
class ConversionHelper {
    companion object {
        fun toIntOrDefault(input: String, default: Int): Int {
            return when (val conversion: Int? = input.trim().toIntOrNull()) {
                null -> default
                else -> conversion
            }
        }

        fun toDoubleOrDefault(input: String, default: Double): Double {
            return when (val conversion: Double? = input.trim().toDoubleOrNull()) {
                null -> default
                else -> conversion
            }
        }

        fun toBooleanOrNull(input: String): Boolean? {
            return when (input.trim().toBoolean()) {
                true -> true
                input.equals("false", ignoreCase = true) -> false
                else -> null
            }
        }

        fun dateFromTimestamp (input: Int?): LocalDateTime? {
            if (input == null) return input
            return Instant.ofEpochSecond(input.toLong())
                .atZone(ZoneId.systemDefault()).toLocalDateTime()
        }
    }
}