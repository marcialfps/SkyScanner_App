package com.miw.skyscanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.miw.skyscanner.ui.flights.FlightsFragment
import com.miw.skyscanner.ui.home.HomeFragment
import com.miw.skyscanner.ui.home.HomeWeatherFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var currentItem: Int = R.id.navigation_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    override fun onBackPressed() {
        // Follow Twitter app model: if not in the home screen, return to the home screen.
        // If in the home screen, let the user exit
        if (currentItem != R.id.navigation_home)
            bottomMenu.selectedItemId = R.id.navigation_home
        else
            super.onBackPressed()

    }

    private fun init() {
        loadFragment(HomeFragment())
        setUpNavigation()
    }

    private fun setUpNavigation() {
        bottomMenu.setOnNavigationItemSelectedListener {
            currentItem = it.itemId
            when(currentItem){
                R.id.navigation_home-> {
                    loadFragment(HomeFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navigation_map-> {
                    loadFragment(HomeFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navigation_flights-> {
                    loadFragment(FlightsFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navigation_weather-> {
                    loadFragment(HomeWeatherFragment())
                    return@setOnNavigationItemSelectedListener true
                }

            }
            false
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment, fragment::class.simpleName)
        transaction.commit()
    }
}
