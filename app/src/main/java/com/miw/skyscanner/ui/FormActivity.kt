package com.miw.skyscanner.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.miw.skyscanner.R
import com.miw.skyscanner.ui.login.LoginFragment
import com.miw.skyscanner.ui.register.RegisterFragment
import kotlinx.android.synthetic.main.activity_form.*

class FormActivity : AppCompatActivity(),
    LoginFragment.OnLoginFragmentInteractionListener,
    RegisterFragment.OnRegisterFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        initialize()
    }

    private fun initialize() {
        supportFragmentManager.beginTransaction().add(fragment_container.id,
            LoginFragment()
        )
            .commit()
    }

    override fun onRegisterButtonClick() {
        val registerFragment = RegisterFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        fragmentTransaction.replace(fragment_container.id, registerFragment)
        fragmentTransaction.commit()
    }

    override fun onLoginButtonClick() {
        val loginFragment = LoginFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        fragmentTransaction.replace(fragment_container.id, loginFragment)
        fragmentTransaction.commit()
    }


}
