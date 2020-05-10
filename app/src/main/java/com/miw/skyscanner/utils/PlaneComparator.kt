package com.miw.skyscanner.utils

import com.miw.skyscanner.model.Plane

class PlaneComparator (private val isArrival: Boolean) : Comparator<Plane> {

    override fun compare(o1: Plane?, o2: Plane?): Int {
        if (o1 != null && o2 != null ) {
            if (isArrival && o1.arrivalTime != null && o2.arrivalTime != null)
                return if (o1.arrivalTime!!.isAfter(o2.arrivalTime)) 1 else -1

            else if (o1.departureTime != null && o2.departureTime != null)
                return if (o1.departureTime!!.isAfter(o2.departureTime)) 1 else -1
        }
        return 0
    }

}