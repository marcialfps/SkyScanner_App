package com.miw.skyscanner.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.miw.skyscanner.R
import com.miw.skyscanner.ui.login.LoginFragment
import com.miw.skyscanner.ui.register.RegisterFragment
import com.miw.skyscanner.utils.Session
import kotlinx.android.synthetic.main.activity_form.*

class FormActivity : AppCompatActivity(),
    LoginFragment.OnLoginFragmentInteractionListener,
    RegisterFragment.OnRegisterFragmentInteractionListener {

    private lateinit var currentFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        if(Session(applicationContext).saveSession) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            initialize()
        }
    }

    private fun initialize() {
        val loginFragment = LoginFragment()
        supportFragmentManager.beginTransaction().add(fragment_container.id,
            loginFragment
        ).commit()
        currentFragment = loginFragment
    }

    override fun onBackPressed() {
        if (currentFragment is LoginFragment) super.onBackPressed()
        else if (currentFragment is RegisterFragment)
            (currentFragment as RegisterFragment).onBackPressed()
    }

    override fun onRegisterButtonClick() {
        val registerFragment = RegisterFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        fragmentTransaction.replace(fragment_container.id, registerFragment)
        fragmentTransaction.commit()
        currentFragment = registerFragment
    }

    override fun onLoginButtonClick() {
        val loginFragment = LoginFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        fragmentTransaction.replace(fragment_container.id, loginFragment)
        fragmentTransaction.commit()
        currentFragment = loginFragment
    }

}
