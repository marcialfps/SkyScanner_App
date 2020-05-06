package com.miw.skyscanner.ui.flights.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.miw.skyscanner.R

class FlightsListAdapter(context: Context, private val data: List<String>,
                         private var isArrivals: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val textAirport: TextView = view.findViewById(R.id.txFlightsArrivalsCardAirport)
        val textDescription: TextView = view.findViewById(R.id.txFlightsArrivalsCardDistance)
        val txTime: TextView = view.findViewById(R.id.txFlightsArrivalsCardTime)
        val cardIcon: ImageView = view.findViewById(R.id.flightsArrivalsCardImage)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view: View = inflater.inflate(R.layout.fragment_flights_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        with(holder as ViewHolder) {
            // Bind the data received to the view created

            // Airport name
            val airport = data[position]
            holder.textAirport.text = airport

            // Description, distances... TODO

            // Card icon
            val iconToUse: Int =
                if (isArrivals) R.drawable.baseline_flight_land_black_24dp
                else R.drawable.baseline_flight_takeoff_black_24dp

            holder.cardIcon.setImageResource(iconToUse)
        }


    }

}