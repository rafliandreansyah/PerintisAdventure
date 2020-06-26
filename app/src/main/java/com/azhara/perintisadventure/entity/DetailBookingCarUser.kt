package com.azhara.perintisadventure.entity

import com.google.firebase.Timestamp

data class DetailBookingCarUser(
    var partnerId: String? = null,
    var carId: String? = null,
    var totalPrice: Long? = null,
    var startDate: Timestamp? = null,
    var endDate: Timestamp? = null,
    var driver: String? = null,
    var pickUpArea: String? = null,
    var statusBooking: Int? = 0 // 0 on progress dan 1 selesai
)