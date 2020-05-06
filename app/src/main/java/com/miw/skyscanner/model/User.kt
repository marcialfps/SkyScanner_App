package com.miw.skyscanner.model

data class User(val username: String, val name: String, val surname: String, val email: String,
                val airportCode: String, val password: String)