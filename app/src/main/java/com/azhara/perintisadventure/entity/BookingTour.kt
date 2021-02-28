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
    var idDetailBookingTourUser: String? = null,
    var userBookingName: String? = null,
    var tourName: String? = null,
    var duration: String? = null,
    var locationTour: String? = null
)