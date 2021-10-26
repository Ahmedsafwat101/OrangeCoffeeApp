package com.orangecoffeeapp.utils.customer

import android.location.Location


class LocationComparator constructor(var origin: Location) :
    Comparator<Location?> {
    override fun compare(left: Location?, right: Location?): Int {
        return origin.distanceTo(left).compareTo(origin.distanceTo(right))
    }
}