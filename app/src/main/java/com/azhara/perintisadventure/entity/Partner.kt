package com.azhara.perintisadventure.entity

import com.google.firebase.firestore.GeoPoint
import com.google.type.LatLng

data class Partner(
    val address: String? = null,
    val email: String? = null,
    val geoPoint: GeoPoint? = null,
    val owner: String? = null,
    val imgUrl: String? = null,
    val phone: String? = null,
    val travelName: String? = null
)