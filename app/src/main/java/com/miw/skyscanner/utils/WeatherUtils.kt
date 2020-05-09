package com.miw.skyscanner.utils

import android.widget.ImageView
import com.miw.skyscanner.R
import com.miw.skyscanner.model.Forecast

fun configureImage(
    forecast: Forecast,
    image: ImageView
) {
    image.setImageResource(when {
        forecast.description.contains("clear") -> R.drawable.clear_sky
        forecast.description.contains("few clouds") -> R.drawable.few_clouds
        forecast.description.contains("clouds") -> R.drawable.scattered_clouds
        forecast.description.contains("shower rain") -> R.drawable.shower_rain
        forecast.description.contains("drizzle") -> R.drawable.drizzle
        forecast.description.contains("rain")  -> R.drawable.rain
        forecast.description.contains("thunderstorm") -> R.drawable.thunderstorm
        forecast.description.contains("snow") -> R.drawable.snow
        else -> R.drawable.mist
    })
}