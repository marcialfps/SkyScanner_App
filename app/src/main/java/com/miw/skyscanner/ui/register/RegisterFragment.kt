package com.miw.skyscanner.ui.register

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.miw.skyscanner.R
import com.miw.skyscanner.ws.CallWebService
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.buttonRegister
import kotlinx.android.synthetic.main.fragment_register.txPassword
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ksoap2.transport.HttpResponseException
import java.lang.RuntimeException

class RegisterFragment : Fragment() {

    private lateinit var listener: OnRegisterFragmentInteractionListener
    var username = ""
    var name = ""
    var surname = ""
    var email = ""
    var airportCode = ""
    var password = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnRegisterFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context debe implementar OnRegisterFragmentInteractionListener")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initialize()
    }

    private fun initialize() {
        buttonLogin.setOnClickListener { listener.onLoginButtonClick() }
        buttonNext.setOnClickListener { next() }
        buttonRegister.setOnClickListener { register() }
    }

    private fun next() {
        username = txUsername.text.toString()
        name = txName.text.toString()
        surname = txName.text.toString()

        if (!(username.isEmpty() or name.isEmpty() or surname.isEmpty())) {
            layoutFirst.visibility = View.INVISIBLE
            layoutSecond.visibility = View.VISIBLE
        }
    }

    private fun register() {
        email = txEmail.text.toString()
        airportCode = txAirport.text.toString()
        password = txPassword.text.toString()
        val passwordRepeat = txPasswordRepeat.text.toString()

        when {
            email.isEmpty() or airportCode.isEmpty() or password.isEmpty()
                    or passwordRepeat.isEmpty() -> txErrorRegister.text = getString(R.string.error_register_empty)
            password != passwordRepeat -> txErrorRegister.text = getString(R.string.error_register_passwords)
            else -> callToRegister()
        }
    }

    private fun callToRegister() {
        layoutLoadingRegister.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = CallWebService().callRegister(username, name, surname, email,
                    airportCode, password)
                withContext(Dispatchers.Main) {
                    txErrorRegister.text = "REGISTER CORRECT TO-DO"
                }
            } catch (e1: HttpResponseException) {
                withContext(Dispatchers.Main) {
                    txErrorRegister.text = when(e1.statusCode) {
                        409 -> getString(R.string.error_register_user_exist)
                        else -> "Unexpected error (${e1.statusCode})"
                    }
                }
                Log.v("response", e1.toString())
            } finally {
                layoutLoadingRegister.visibility = View.INVISIBLE
            }
        }
    }

    interface OnRegisterFragmentInteractionListener {
        fun onLoginButtonClick()
    }
}