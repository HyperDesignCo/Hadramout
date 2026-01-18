package com.hyperdesign.myapplication.presentation.common.wedgits

import com.google.android.gms.maps.model.LatLng

fun LatLng.distanceMetersTo(other: LatLng): Double {
    val earthRadius = 6371000.0

    val lat1 = Math.toRadians(this.latitude)
    val lat2 = Math.toRadians(other.latitude)
    val dLat = Math.toRadians(other.latitude - this.latitude)
    val dLon = Math.toRadians(other.longitude - this.longitude)

    val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(lat1) * Math.cos(lat2) *
            Math.sin(dLon / 2) * Math.sin(dLon / 2)

    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

    return earthRadius * c
}