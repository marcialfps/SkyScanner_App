package com.miw.skyscanner

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.miw.skyscanner.fragments.LoginFragment
import com.miw.skyscanner.fragments.RegisterFragment
import kotlinx.android.synthetic.main.activity_form.*

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

class FormActivity : AppCompatActivity(),
    LoginFragment.OnLoginFragmentInteractionListener,
    RegisterFragment.OnRegisterFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        initialize()
    }

    private fun initialize() {
        supportFragmentManager.beginTransaction().add(fragment_container.id, LoginFragment())
            .commit()
    }

    override fun onRegisterButtonClick() {
        val registerFragment = RegisterFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        fragmentTransaction.replace(fragment_container.id, registerFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onLoginButtonClick() {
        val loginFragment = LoginFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        fragmentTransaction.replace(fragment_container.id, loginFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }


}
