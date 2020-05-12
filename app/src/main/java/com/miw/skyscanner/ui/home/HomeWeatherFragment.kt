package com.miw.skyscanner.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.miw.skyscanner.R
import com.miw.skyscanner.model.AirportForecastList
import com.miw.skyscanner.utils.Session
import com.miw.skyscanner.utils.configureImage
import kotlinx.android.synthetic.main.fragment_main_weather.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class HomeWeatherFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_weather, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpWeather()
        getWeather()
    }

    private fun setUpWeather() {
        weatherCard.setOnClickListener( View.OnClickListener {
            val bottomMenu = activity?.findViewById<BottomNavigationView>(R.id.bottomMenu)
            bottomMenu?.selectedItemId = R.id.navigation_weather
        })
    }

    private fun getWeather() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = context?.let { Session(it).airport }?.let {
                    AirportForecastList.requestCurrentWeather(it)
                }
                if (result != null) {
                    withContext(Dispatchers.Main) {
                        txWeatherTemperature.text = "${result.temperature.toInt()} ºc"
                        val dateFormatter = SimpleDateFormat("HH:mm, EEE dd MMM YYYY", Locale.getDefault())
                        txDate.text = dateFormatter.format(Date())
                        txWeatherLocation.text = context?.let {
                            val city = Session(it).city
                            if (city.isBlank()) getString(R.string.weather_alternate_text)
                            else city
                        }
                        configureImage(result, weatherImage)
                    }
                }
            } catch (e1: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, getString(R.string.error_forecast), Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}