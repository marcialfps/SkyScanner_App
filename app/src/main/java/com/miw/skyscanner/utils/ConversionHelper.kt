package com.miw.skyscanner.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

// Additional methods to help when building model entities from SOAP responses
class ConversionHelper {
    companion object {
        private val hourFormatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("HH:mm")
        private val dateFormatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("dd/MM/yy")

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

        fun formatDateTimeToHour (input: LocalDateTime): String {
            return hourFormatter.format(input)
        }

        fun formatDateTimeToDate (input: LocalDateTime): String {
            return dateFormatter.format(input)
        }
    }
}