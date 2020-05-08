package com.miw.skyscanner.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.miw.skyscanner.R
import com.miw.skyscanner.ui.flights.FlightsCollectionAdapter
import com.miw.skyscanner.ui.flights.FlightsFragment
import com.miw.skyscanner.ui.home.HomeFragment
import com.miw.skyscanner.ui.home.HomeWeatherFragment
import com.miw.skyscanner.ui.map.MapFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_flights.*
import kotlinx.android.synthetic.main.fragment_flights_list.*

class MainActivity : AppCompatActivity() {

    private var currentItem: Int = R.id.navigation_home
    private lateinit var currentFragment: Fragment

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
            moveTaskToBack(true)

    }

    private fun init() {
        title = resources.getString(R.string.bottom_menu_home)
        setUpNavigation()
        loadFragment(HomeFragment())

    }

    private fun setUpNavigation() {
        bottomMenu.setOnNavigationItemSelectedListener {

            // Of the user clicks the current menu item, do nothing
            if (currentItem == it.itemId) {
                // If the user clicks the current menu item and we are in the flights list,
                // scroll to top both arrivals and departures lists
                if (currentItem == R.id.navigation_flights) {

                    val adapter: FlightsCollectionAdapter =
                        (currentFragment.flightsViewPager.adapter as FlightsCollectionAdapter)

                    adapter.innerFragments.forEachIndexed { index, list ->
                        val listManager =
                            list.flightsListRecyclerView.layoutManager!!
                        if (index == adapter.currentFragment)
                            listManager.smoothScrollToPosition(flightsListRecyclerView, RecyclerView.State(), 0)
                        else
                            listManager.scrollToPosition(0)

                    }
                }
                return@setOnNavigationItemSelectedListener false
            }

            currentItem = it.itemId

            // Change action bar title and foreground fragment
            when(currentItem){
                R.id.navigation_home -> {
                    title = resources.getString(R.string.bottom_menu_home)
                    loadFragment(HomeFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navigation_map -> {
                    title = resources.getString(R.string.bottom_menu_map)
                    loadFragment(MapFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navigation_flights -> {
                    title = resources.getString(R.string.bottom_menu_flights)
                    loadFragment(FlightsFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navigation_weather -> {
                    title = resources.getString(R.string.bottom_menu_weather)
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

        currentFragment = fragment
    }
}
