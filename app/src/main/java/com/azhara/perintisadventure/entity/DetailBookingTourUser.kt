package com.azhara.perintisadventure.entity

import com.google.firebase.Timestamp

data class DetailBookingTourUser(
    var date: Timestamp? = null,
    var tourId: String? = null,
    var totalPrice: Long? = 0,
    var pickupArea: String? = null,
    var statusBooking: Int? = 0
)