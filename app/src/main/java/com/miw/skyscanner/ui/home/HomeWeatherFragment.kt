package com.miw.skyscanner.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.miw.skyscanner.R
import com.miw.skyscanner.ws.CallWebService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ksoap2.transport.HttpResponseException

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
        getWeather()
    }

    private fun getWeather() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = CallWebService().callCurrentWeather("LEMD")
                if (result != null) {
                    withContext(Dispatchers.Main) {

                    }
                }
            } catch (e1: HttpResponseException) {
                withContext(Dispatchers.Main) {

                }
                Log.e("forecastFragment", e1.toString())
            }
        }
    }
}