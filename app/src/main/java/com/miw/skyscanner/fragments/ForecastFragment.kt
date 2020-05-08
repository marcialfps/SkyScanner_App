package com.miw.skyscanner.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.miw.skyscanner.R
import com.miw.skyscanner.ws.CallWebService
import kotlinx.android.synthetic.main.fragment_forecast.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ksoap2.transport.HttpResponseException
import java.text.DateFormat
import java.util.*

class ForecastFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
       /* if (context is OnLoginFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnLoginFragmentInteractionListener")
        }*/
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initialize()
    }

    private fun initialize() {
        /*buttonRegister.setOnClickListener { listener.onRegisterButtonClick() }
        buttonGo.setOnClickListener { login() }*/
        getForecast()
    }

    private fun getForecast() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = CallWebService().callGetForecast("LEMD")
                if (result != null) {
                    withContext(Dispatchers.Main) {
                        val df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault())
                        txTime.text = df.format(result.get(0).time)
                        txCity.text = "Madrid TO-DO"

                        result.forEach {

                        }

                    }
                }
            } catch (e1: HttpResponseException) {
                withContext(Dispatchers.Main) {
                    /*txError.text = if (e1.statusCode === 403) getString(R.string.error_login_credentials)
                    else "Unexpected error (${e1.statusCode})"*/
                }
                Log.v("response", e1.toString())
            } finally {
                layoutLoadingWeather.visibility = View.INVISIBLE
            }
        }
    }

    /*interface OnLoginFragmentInteractionListener {
        fun onRegisterButtonClick()
    }*/
}