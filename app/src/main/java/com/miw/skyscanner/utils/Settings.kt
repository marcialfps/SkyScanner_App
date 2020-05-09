package com.miw.skyscanner.utils

import android.content.Context
import android.content.SharedPreferences
import com.miw.skyscanner.model.User

class Session(context: Context) {
    companion object {
        const val PREFS_NAME = "SessionPrefsFile"
        const val SESSION_USERNAME = "Username"
        const val SESSION_NAME = "Name"
        const val SESSION_SURNAME = "Surname"
        const val SESSION_EMAIL = "Email"
        const val SESSION_AIRPORT = "Airport"
        const val PREF_DEFAULT: String = ""
    }
    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

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
}