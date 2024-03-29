package com.miw.skyscanner.ui.home

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.miw.skyscanner.R
import com.miw.skyscanner.ui.home.flights.HomeArrivalsFragment
import com.miw.skyscanner.ui.home.flights.HomeDeparturesFragment
import com.miw.skyscanner.utils.Session
import kotlinx.android.synthetic.main.fragment_main.*

class HomeFragment : Fragment() {

    private lateinit var listener: OnLogoutClickListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnLogoutClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnLogoutClickListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Create child fragments: weather, arrivals and departures summary
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.arrivalsFragmentContainer, HomeArrivalsFragment())
        transaction.add(R.id.departuresFragmentContainer, HomeDeparturesFragment())
        transaction.add(R.id.weatherFragmentContainer, HomeWeatherFragment())
        transaction.commit()
        initialize()
    }

    private fun initialize() {
        txAirport.text = context?.let { Session(it).airportName }
        setUpToolsMenu()
    }

    private fun setUpToolsMenu() {
        buttonTools.setOnClickListener {view ->
            val popup = PopupMenu(context, view)
            val inflater: MenuInflater = popup.menuInflater
            inflater.inflate(R.menu.tools_menu, popup.menu)
            popup.show()
            popup.setOnMenuItemClickListener {  listener.onMenuItemClick(it) }
        }
    }

    interface OnLogoutClickListener {
        fun onMenuItemClick(item: MenuItem): Boolean
    }

}
