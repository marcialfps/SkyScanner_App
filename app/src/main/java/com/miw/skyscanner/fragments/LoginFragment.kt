package com.miw.skyscanner.fragments

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.miw.skyscanner.R
import com.miw.skyscanner.utils.WSUtils
import com.miw.skyscanner.ws.CallWebService
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ksoap2.transport.HttpResponseException
import java.lang.Exception

class LoginFragment : Fragment() {

    private lateinit var listener: OnLoginFragmentInteractionListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnLoginFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnLoginFragmentInteractionListener")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initialize()
    }

    private fun initialize() {
        buttonRegister.setOnClickListener { listener.onRegisterButtonClick() }
        buttonGo.setOnClickListener { login() }
    }

    private fun login() {
        val user = txUser.text.toString()
        val password = txPassword.text.toString()

        if (user.isEmpty() or password.isEmpty()) {
            txError.text = getString(R.string.error_login_empty)
        } else {
            layoutLoading.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val result = CallWebService().callLogin(user, password)
                    if (result != null) {
                        withContext(Dispatchers.Main) {
                            txError.text = "LOGIN CORRECT TO-DO"
                        }
                    }
                } catch (e1: HttpResponseException) {
                    withContext(Dispatchers.Main) {
                        txError.text = if (e1.statusCode === 403) getString(R.string.error_login_credentials)
                            else "Unexpected error (${e1.statusCode})"
                    }
                    Log.v("response", e1.toString())
                } finally {
                    layoutLoading.visibility = View.INVISIBLE
                }
            }
        }
    }

    interface OnLoginFragmentInteractionListener {
        fun onRegisterButtonClick()
    }
}