package com.miw.skyscanner.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.autofill.AutofillValue
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.miw.skyscanner.R
import com.miw.skyscanner.ui.MainActivity
import com.miw.skyscanner.utils.Session
import com.miw.skyscanner.utils.WSUtils
import com.miw.skyscanner.utils.isInternetAvailable
import com.miw.skyscanner.ws.CallWebService
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ksoap2.transport.HttpResponseException

class LoginFragment : Fragment() {

    private lateinit var listener: OnLoginFragmentInteractionListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
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
        txPassword.setOnEditorActionListener (SubmitOnEditorActionListener(this))
        txUser.setAutofillHints(View.AUTOFILL_HINT_USERNAME)
        txPassword.setAutofillHints(View.AUTOFILL_HINT_PASSWORD)

        CoroutineScope(Dispatchers.IO).launch {
            context?.let {
                if(!isInternetAvailable(it)) {
                    withContext(Dispatchers.Main) {
                        txError.text = getString(R.string.no_internet_connection)
                        changeFormEnabled(false)
                    }
                }
            }
        }
    }

    private fun login() {
        val user = txUser.text.toString()
        val password = txPassword.text.toString()

        if (user.isEmpty() or password.isEmpty()) {
            txError.text = getString(R.string.error_login_empty)
        } else {
            layoutLoading.visibility = View.VISIBLE
            changeFormEnabled(false)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val result = CallWebService().callLogin(user, password)
                    withContext(Dispatchers.Main) {
                        context?.let {
                            Session(it).username = result.username
                            Session(it).name = result.name
                            Session(it).surname = result.surname
                            Session(it).email = result.email
                            Session(it).airport = result.airportCode
                        }
                        val intent = Intent(context, MainActivity::class.java)
                        startActivity(intent)
                    }
                } catch (e1: HttpResponseException) {
                    withContext(Dispatchers.Main) {
                        txError.text = if (e1.statusCode == 403) getString(R.string.error_login_credentials)
                            else "Unexpected error (${e1.statusCode})"
                        changeFormEnabled(true)
                    }
                    Log.v("response", e1.toString())
                } finally {
                    layoutLoading.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun changeFormEnabled(isEnabled: Boolean) {
        txUser.isEnabled = isEnabled
        txPassword.isEnabled = isEnabled
        buttonGo.isEnabled = isEnabled
        buttonRegister.isEnabled = isEnabled
    }

    interface OnLoginFragmentInteractionListener {
        fun onRegisterButtonClick()
    }

    // Login when submitting the password edit text
    class SubmitOnEditorActionListener (private val loginFragment: LoginFragment) : TextView.OnEditorActionListener {
        override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
            return when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {loginFragment.login(); true
                }
                else -> false
            }
        }
    }
}