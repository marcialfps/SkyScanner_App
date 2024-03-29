package com.miw.skyscanner.utils

import android.content.Context
import android.content.SharedPreferences

class Session(context: Context) {
    companion object {
        const val PREFS_NAME = "SessionPrefsFile"
        const val SAVE_SESSION = "SaveSession"
        const val SESSION_USERNAME = "Username"
        const val SESSION_NAME = "Name"
        const val SESSION_SURNAME = "Surname"
        const val SESSION_EMAIL = "Email"
        const val SESSION_AIRPORT = "Airport"
        const val SESSION_AIRPORT_NAME = "AirportName"
        const val SESSION_CITY = "City"
        const val PREF_DEFAULT: String = ""
        const val SAVE_SESSION_DEFAULT: Boolean = false
    }
    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var saveSession: Boolean
        get() = preferences.getBoolean(SAVE_SESSION, SAVE_SESSION_DEFAULT)
        set(value) {
            val editor = preferences.edit()
            editor.putBoolean(SAVE_SESSION, value)
            editor.apply()
        }

    var username: String
        get() = preferences.getString(SESSION_USERNAME, PREF_DEFAULT) ?: PREF_DEFAULT
        set(value) {
            val editor = preferences.edit()
            editor.putString(SESSION_USERNAME, value)
            editor.apply()
        }

    var name: String
        get() = preferences.getString(SESSION_NAME, PREF_DEFAULT) ?: PREF_DEFAULT
        set(value) {
            val editor = preferences.edit()
            editor.putString(SESSION_NAME, value)
            editor.apply()
        }

    var surname: String
        get() = preferences.getString(SESSION_SURNAME, PREF_DEFAULT) ?: PREF_DEFAULT
        set(value) {
            val editor = preferences.edit()
            editor.putString(SESSION_SURNAME, value)
            editor.apply()
        }

    var email: String
        get() = preferences.getString(SESSION_EMAIL, PREF_DEFAULT) ?: PREF_DEFAULT
        set(value) {
            val editor = preferences.edit()
            editor.putString(SESSION_EMAIL, value)
            editor.apply()
        }

    var airport: String
        get() = preferences.getString(SESSION_AIRPORT, PREF_DEFAULT) ?: PREF_DEFAULT
        set(value) {
            val editor = preferences.edit()
            editor.putString(SESSION_AIRPORT, value)
            editor.apply()
        }

    var airportName: String
        get() = preferences.getString(SESSION_AIRPORT_NAME, PREF_DEFAULT) ?: PREF_DEFAULT
        set(value) {
            val editor = preferences.edit()
            editor.putString(SESSION_AIRPORT_NAME, value)
            editor.apply()
        }

    var city: String
        get() = preferences.getString(SESSION_CITY, PREF_DEFAULT) ?: PREF_DEFAULT
        set(value) {
            val editor = preferences.edit()
            editor.putString(SESSION_CITY, value)
            editor.apply()
        }
}