package com.miw.skyscanner.ui.weather

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.miw.skyscanner.R
import com.miw.skyscanner.model.AirportForecastList
import com.miw.skyscanner.model.Forecast
import com.miw.skyscanner.utils.Session
import com.miw.skyscanner.utils.configureImage
import kotlinx.android.synthetic.main.fragment_forecast.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class ForecastFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forecast, container, false)
    }

    private lateinit var selectedForecast: Forecast
    private lateinit var forecasts: List<Forecast>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getForecast()
    }

    private fun getForecast() {
       CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = context?.let { Session(it).airport }?.let {
                    AirportForecastList.requestForecast(it)?.forecasts
                }
                if (result != null) {
                    withContext(Dispatchers.Main) {
                        forecasts = result
                        selectedForecast = forecasts[0]
                        val dayFormatter = SimpleDateFormat("dd", Locale.getDefault())
                        val today = dayFormatter.format(selectedForecast.time*1000)
                        var counterTomorrow = 0
                        var counterTAfterTomorrow = 0
                        forecasts.forEachIndexed { index, forecast ->
                            if (index < 5) {
                                val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
                                when (index) {
                                    0 -> txHour1.text = formatter.format(Date(forecast.time*1000))
                                    1 -> txHour2.text = formatter.format(Date(forecast.time*1000))
                                    2 -> txHour3.text = formatter.format(Date(forecast.time*1000))
                                    3 -> txHour4.text = formatter.format(Date(forecast.time*1000))
                                    4 -> txHour5.text = formatter.format(Date(forecast.time*1000))
                                }
                            }

                            val day = dayFormatter.format(forecast.time*1000)

                            if (day.toInt() == today.toInt()+1 && counterTomorrow == 0) {
                                Log.v("response", "${day.toInt()} === ${today.toInt()+1}")
                                configureImage(forecast, imageTomorrow)
                                configureWeekDay(forecast, txTomorrow)
                                counterTomorrow++
                            } else if (day.toInt() == today.toInt()+2 && counterTAfterTomorrow == 0) {
                                configureImage(forecast, imageAfterTomorrow)
                                configureWeekDay(forecast, txAfterTomorrow)
                                counterTAfterTomorrow++
                            }
                        }

                        configureSeekBar()
                        showForecast()
                        progressBar.visibility = View.INVISIBLE
                        layoutContent.visibility = View.VISIBLE
                    }
                }
            } catch (e1: Exception) {
                withContext(Dispatchers.Main) {
                    if (txErrorForecast != null && progressBar != null)
                    {
                        txErrorForecast.text = getString(R.string.error_forecast)
                        progressBar.visibility = View.INVISIBLE
                    }
                }
                Log.e("forecastFragment", e1.toString())
            }
        }

    }

    private fun configureSeekBar() {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                selectedForecast = forecasts[progress]
                showForecast()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun showForecast() {
        val dateFormatter = SimpleDateFormat("EEEE, HH:mm", Locale.getDefault())
        txCity.text = context?.let {
            // If we have no city, show a generic "Weather" text
            val city = Session(it).city
            if (city.isBlank()) getString(R.string.weather_alternate_text)
            else city
        }
        txTime.text =
            "${dateFormatter.format(Date(selectedForecast.time * 1000)).capitalize()}, ${selectedForecast.description}"
        txTemperature.text = selectedForecast.temperature.toInt().toString()
        txPrecipation.text = "${selectedForecast.humidity}% ${getString(R.string.humidity)}"
        txWind.text = "${selectedForecast.windSpeed} m/s ${getString(R.string.winds)}"
        txPressure.text = "${selectedForecast.pressure} hPa"
        txWindDirection.text = "${selectedForecast.windDirection} ${getString(R.string.degrees)}"
        txTempMin.text = "${selectedForecast.temperatureMin} ${getString(R.string.weather_degrees_celsius)}"
        txTempMax.text = "${selectedForecast.temperatureMax} ${getString(R.string.weather_degrees_celsius)}"
        configureImage(selectedForecast, imageSky)
    }

    private fun configureWeekDay(forecast: Forecast, textWeekDay: TextView) {
        val weekDayFormatter = SimpleDateFormat("EEEE", Locale.getDefault())
        textWeekDay.text = weekDayFormatter.format(forecast.time*1000).toUpperCase(Locale.getDefault())
    }
}