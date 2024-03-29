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
import com.miw.skyscanner.utils.ConversionHelper
import java.time.LocalDateTime


class FlightsListAdapter(context: Context, private val data: List<Plane>,
                         private var isArrivals: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val flightCardDescriptionArrivals = context.getString(R.string.arrivals_distance_left)
    private val flightCardDescriptionDepartures = context.getString(R.string.departures_distance_covered)
    private val flightCardDateToday = context.getString(R.string.flights_today)
    private val primaryColor = context.getColor(R.color.colorPrimary)
    private val altColor = context.getColor(R.color.secondaryTextLight)


    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val textAirport: TextView = view.findViewById(R.id.txFlightsArrivalsCardAirport)
        val textDescription: TextView = view.findViewById(R.id.txFlightsArrivalsCardDistance)
        val txTime: TextView = view.findViewById(R.id.txFlightsArrivalsCardTime)
        val txDate: TextView = view.findViewById(R.id.txFlightsArrivalsCardDate)
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

            val airportCode: String
            val distanceInformation: Int
            val date: LocalDateTime
            // If the plane has the data we need, we show it
            if (isArrivals){
                // We have filtered the results after recovering the data from the API,
                // so we can force calls
                airportCode = plane.departureAirportCode!!
                distanceInformation = plane.arrivalDistance!!
                date = plane.arrivalTime!!
            }
            else {
                airportCode = plane.arrivalAirportCode!!
                distanceInformation = plane.departureDistance!!
                date = plane.departureTime!!
            }

            this.textAirport.text = airportCode
            this.textDescription.text =
                if (isArrivals) String.format(flightCardDescriptionArrivals, distanceInformation)
                else String.format(flightCardDescriptionDepartures, distanceInformation)
            this.txTime.text = ConversionHelper.formatDateTimeToHour(date)

            // If date is today, change appearance
            if (ConversionHelper.isDateToday(date)) {
                this.txDate.text = flightCardDateToday
                this.txDate.setTextColor(primaryColor)
            }
            else {
                this.txDate.text = ConversionHelper.formatDateTimeToDate(date)
                this.txDate.setTextColor(altColor)
            }

            // Card icon
            this.cardIcon.setImageResource(
                if (isArrivals) R.drawable.landing
                else R.drawable.take_off
            )
        }


    }

}