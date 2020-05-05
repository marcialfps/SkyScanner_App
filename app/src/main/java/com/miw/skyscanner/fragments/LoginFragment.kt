package com.miw.skyscanner.fragments

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.miw.skyscanner.R
import com.miw.skyscanner.utils.WSUtils
import com.miw.skyscanner.ws.CallWebService
import kotlinx.android.synthetic.main.login_fragment.*
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
            throw RuntimeException("$context debe implementar OnFirstFragmentInteractionListener")
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
            //Send request to the service
            loginUser().execute(user, password)
        }
    }

    interface OnLoginFragmentInteractionListener {
        fun onRegisterButtonClick()
    }

    inner class loginUser: AsyncTask<String, String, String>() {
        override fun doInBackground(vararg params: String?): String {
            val response = CallWebService().callLogin(params[0], params[1])
            Log.v("response", "response== $response")
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Log.v("response", "OnPostresponse== $result")
            try {
                Log.i("response","Login correct")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}