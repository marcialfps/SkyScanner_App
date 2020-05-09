package com.miw.skyscanner.ui.flights.list

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.miw.skyscanner.R
import com.miw.skyscanner.model.Plane


class FlightsListAdapter(context: Context, private val data: List<Plane>,
                         private var isArrivals: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val flightCardDescriptionArrivals = context.getString(R.string.arrivals_distance_left)
    private val flightCardDescriptionDepartures = context.getString(R.string.departures_distance_covered)


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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        with(holder as ViewHolder) {
            // Bind the data received to the view created
            val plane = data[position]

            var airportCode: String? = null
            var distanceInformation: Int? = null
            var timeInformation: String? = null
            // If the plane has the data we need, we show it
            if (isArrivals){
                if (plane.departureAirportCode != null && plane.departureDistance != null
                    && plane.departureTime != null) {
                    airportCode = plane.departureAirportCode
                    distanceInformation = plane.departureDistance
                    timeInformation = "${plane.departureTime!!.hour}:${plane.departureTime!!.minute}"
                }
            }
            else if (plane.arrivalAirportCode != null && plane.arrivalDistance != null
                && plane.arrivalTime != null) {
                airportCode = plane.arrivalAirportCode
                distanceInformation = plane.arrivalDistance
                timeInformation = "${plane.arrivalTime!!.hour}:${plane.arrivalTime!!.minute}"
            }

            if (airportCode != null &&
                distanceInformation != null && timeInformation != null) {

                holder.textAirport.text = airportCode
                holder.textDescription.text =
                    if (isArrivals) String.format(flightCardDescriptionArrivals, distanceInformation)
                    else String.format(flightCardDescriptionDepartures, distanceInformation)
                holder.txTime.text = timeInformation
            }

            // Card icon
            val iconToUse: Int =
                if (isArrivals) R.drawable.baseline_flight_land_black_24dp
                else R.drawable.baseline_flight_takeoff_black_24dp

            holder.cardIcon.setImageResource(iconToUse)
        }


    }

}