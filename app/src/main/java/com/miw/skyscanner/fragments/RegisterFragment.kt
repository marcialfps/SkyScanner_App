package com.miw.skyscanner.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.miw.skyscanner.R
import kotlinx.android.synthetic.main.register_fragment.*
import java.lang.RuntimeException

class RegisterFragment : Fragment() {

    private lateinit var listener: OnRegisterFragmentInteractionListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.register_fragment, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnRegisterFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context debe implementar OnSecondFragmentInteractionListener")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initialize()
    }

    private fun initialize() {
        buttonLogin.setOnClickListener { listener.onLoginButtonClick() }
        buttonNext.setOnClickListener { next() }
    }

    private fun next() {
        val username = txUsername.text.toString()
        val name = txName.text.toString()
        val surname = txName.text.toString()

        if (username.isEmpty() or name.isEmpty() or surname.isEmpty()) {
            txErrorRegister.text = getString(R.string.error_register_empty)
        } else {

        }
    }

    interface OnRegisterFragmentInteractionListener {
        fun onLoginButtonClick()
    }
}