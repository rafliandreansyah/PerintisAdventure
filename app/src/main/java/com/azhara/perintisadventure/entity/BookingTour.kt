package com.azhara.perintisadventure.entity

import com.google.firebase.Timestamp

data class BookingTour(
    var userId: String? = null,
    var tourId: String? = null,
    var bookingListUserId: String? = null,
    var totalPrice: Long? = null,
    var dateTour: Timestamp? = null,
    var pickUpArea: String? = null,
    var statusPayment: Boolean? = false,
    var statusBooking: Int? = 0, // 0 on Progress dan 1 Selesai
    var uploadProofPayment: Boolean? = false,
    var imgUrlProofPayment: String? = null
)