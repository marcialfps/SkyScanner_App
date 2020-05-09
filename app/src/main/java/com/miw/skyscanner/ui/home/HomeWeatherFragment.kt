package com.miw.skyscanner.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.miw.skyscanner.R
import com.miw.skyscanner.model.User
import com.miw.skyscanner.utils.Session
import com.miw.skyscanner.utils.configureImage
import com.miw.skyscanner.ws.CallWebService
import kotlinx.android.synthetic.main.fragment_main_weather.*
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
                val result = context?.let { Session(it).airport }?.let {
                    CallWebService().callCurrentWeather(
                        it
                    )
                }
                if (result != null) {
                    withContext(Dispatchers.Main) {
                        // TODO me cascó una vez la app porque se ve que era null el text asignado
                        txWeatherTemperature.text = "${result.temperature.toInt()} ºc"
                        configureImage(result, weatherImage)
                    }
                }
            } catch (e1: HttpResponseException) {
                withContext(Dispatchers.Main) {

                }
                Log.e("homeWeatherFragment", e1.toString())
            }
        }
    }
}