package com.miw.skyscanner.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.miw.skyscanner.R
import com.miw.skyscanner.model.User

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Create child fragments: weather, arrivals and departures summary
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.weatherFragmentContainer, HomeWeatherFragment())
        transaction.add(R.id.arrivalsFragmentContainer, HomeArrivalsFragment())
        transaction.add(R.id.departuresFragmentContainer, HomeDeparturesFragment())
        transaction.commit()
    }
}