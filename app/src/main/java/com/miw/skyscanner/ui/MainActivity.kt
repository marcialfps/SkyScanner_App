package com.miw.skyscanner.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.miw.skyscanner.R
import com.miw.skyscanner.ui.flights.FlightsCollectionAdapter
import com.miw.skyscanner.ui.flights.FlightsFragment
import com.miw.skyscanner.ui.home.HomeFragment
import com.miw.skyscanner.ui.map.MapFragment
import com.miw.skyscanner.ui.weather.ForecastFragment
import com.miw.skyscanner.utils.Session
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_flights.*
import kotlinx.android.synthetic.main.fragment_flights_list.*
import kotlinx.android.synthetic.main.fragment_main.*
import java.time.LocalDate
import java.util.*

class MainActivity : AppCompatActivity(), HomeFragment.OnLogoutClickListener {

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

            // Of the user clicks the current menu item, do not change fragments
            if (currentItem == it.itemId) {


                // If the user clicks the current menu item and we are in home screen,
                // scroll to top
                if (currentItem == R.id.navigation_home) {
                    currentFragment.homeScrollView.smoothScrollTo(0,0)
                }




                // If the user clicks the current menu item and we are in the flights list,
                // scroll to top both arrivals and departures lists
                if (currentItem == R.id.navigation_flights) {

                    val adapter: FlightsCollectionAdapter =
                        (currentFragment.flightsViewPager.adapter as FlightsCollectionAdapter)

                    adapter.innerFragments.forEachIndexed { index, list ->
                        val listManager =
                            list.flightsListRecyclerView.layoutManager!!
                        if (index == adapter.currentFragmentIndex)
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
                    loadFragment(ForecastFragment())
                    return@setOnNavigationItemSelectedListener true
                }

            }
            false
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()

        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
        transaction.replace(R.id.fragmentContainer, fragment, fragment::class.simpleName)
        transaction.commit()

        currentFragment = fragment
    }
    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                val editor = deleteSharedPreferences(Session.PREFS_NAME)
                val intent = Intent(this, FormActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent)
                finish()
                true
            }
            R.id.about -> {
                openAboutDialog()
                true
            }
            else -> false
        }
    }

    private fun openAboutDialog () {
        val dialogBuilder = AlertDialog.Builder(this@MainActivity)
        val appName = getString(R.string.app_name)
        val aboutMessage =
            getString(R.string.about_message, appName, LocalDate.now().year.toString())
        dialogBuilder.setTitle(appName)
        dialogBuilder.setMessage(aboutMessage)
        dialogBuilder.create().show()
    }

}
