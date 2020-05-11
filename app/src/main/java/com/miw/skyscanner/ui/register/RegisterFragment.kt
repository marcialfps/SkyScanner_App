package com.miw.skyscanner.ui.register

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.miw.skyscanner.R
import com.miw.skyscanner.model.AirportForecastList
import com.miw.skyscanner.ui.MainActivity
import com.miw.skyscanner.utils.Session
import com.miw.skyscanner.utils.isInternetAvailable
import com.miw.skyscanner.ws.CallWebService
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ksoap2.transport.HttpResponseException

const val MIN_USERNAME_LENGTH = 5
const val MIN_PASSWORD_LENGTH = 5


class RegisterFragment : Fragment() {

    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()

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
        txAirport.onRightDrawableClicked {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.world-airport-codes.com/"))
            if (activity?.packageManager?.let { it1 -> intent.resolveActivity(it1) } != null) startActivity(intent)
        }
    }

        fun EditText.onRightDrawableClicked(onClicked: (view: EditText) -> Unit) {
        this.setOnTouchListener { v, event ->
            var hasConsumed = false
            if (v is EditText) {
                if (event.x >= v.width - v.totalPaddingRight) {
                    if (event.action == MotionEvent.ACTION_UP) {
                        onClicked(this)
                    }
                    hasConsumed = true
                }
            }
            hasConsumed
        }
    }


    private fun next() {
        username = txUsername.text.toString()
        name = txName.text.toString()
        surname = txName.text.toString()

        if (!isInternetAvailable(context!!)) {
            txErrorRegister1.text = getString(R.string.no_internet_connection)
            return
        }

        when {
            username.isBlank() or name.isBlank() or surname.isBlank() -> {
                txErrorRegister1.text = getString(R.string.error_register_empty)
            }
            username.length < MIN_USERNAME_LENGTH -> txErrorRegister1.text =
                getString(R.string.error_register_username_short, MIN_USERNAME_LENGTH)
            else -> {
                layoutFirst.visibility = View.INVISIBLE
                layoutSecond.visibility = View.VISIBLE
            }
        }
    }

    private fun register() {
        email = txEmail.text.toString()
        airportCode = txAirport.text.toString()
        password = txPassword.text.toString()
        val passwordRepeat = txPasswordRepeat.text.toString()

        if (!isInternetAvailable(context!!)) {
            txErrorRegister2.text = getString(R.string.no_internet_connection)
            return
        }

        when {
            email.isBlank() or airportCode.isBlank() or password.isBlank()
                    or passwordRepeat.isBlank()
            -> txErrorRegister2.text = getString(R.string.error_register_empty)

            !email.matches(emailPattern) -> txErrorRegister2.text = getString(R.string.error_register_email)
            password != passwordRepeat -> txErrorRegister2.text = getString(R.string.error_register_passwords)
            password.length < MIN_PASSWORD_LENGTH -> txErrorRegister2.text =
                getString(R.string.error_register_password_short, MIN_PASSWORD_LENGTH)

            else -> callToRegister()
        }
    }

    private fun callToRegister() {
        layoutLoadingRegister.visibility = View.VISIBLE
        changeFormEnabled(false)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = CallWebService().callRegister(username, name, surname, email,
                    airportCode, password)
                val airportInfo = context?.let { Session(it).airport }?.let {
                    AirportForecastList.requestAirportInfo(result.airportCode)
                }
                withContext(Dispatchers.Main) {
                    context?.let {
                        Session(it).username = result.username
                        Session(it).name = result.name
                        Session(it).surname = result.surname
                        Session(it).email = result.email
                        Session(it).airport = result.airportCode
                        Session(it).airportName = airportInfo?.name ?: ""
                        Session(it).city = airportInfo?.city ?: ""
                    }
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                }
            } catch (e1: HttpResponseException) {
                withContext(Dispatchers.Main) {
                    txErrorRegister2.text = when(e1.statusCode) {
                        404 -> getString(R.string.error_register_airport)
                        409 -> getString(R.string.error_register_user_exist)
                        else -> "Unexpected error (${e1.statusCode})"
                    }
                    changeFormEnabled(true)
                }
                Log.v("response", e1.toString())
            } finally {
                layoutLoadingRegister.visibility = View.INVISIBLE
            }
        }
    }

    private fun changeFormEnabled(isEnabled: Boolean) {
        txEmail.isEnabled = isEnabled
        txAirport.isEnabled = isEnabled
        txPassword.isEnabled = isEnabled
        txPasswordRepeat.isEnabled = isEnabled
        buttonRegister.isEnabled = isEnabled
    }

    interface OnRegisterFragmentInteractionListener {
        fun onLoginButtonClick()
    }
}