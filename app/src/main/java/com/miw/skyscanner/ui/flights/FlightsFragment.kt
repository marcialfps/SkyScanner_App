package com.miw.skyscanner.ui.flights

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.miw.skyscanner.R
import kotlinx.android.synthetic.main.fragment_flights.*


private const val NUMBER_OF_TABS = 2

class FlightsFragment () : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_flights, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null){
            val flightsCollectionAdapter = FlightsCollectionAdapter(activity!!, NUMBER_OF_TABS)
            flightsViewPager.adapter = flightsCollectionAdapter
        }
    }

}
