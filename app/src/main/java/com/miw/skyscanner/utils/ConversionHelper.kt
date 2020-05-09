package com.miw.skyscanner.utils

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
    }
}