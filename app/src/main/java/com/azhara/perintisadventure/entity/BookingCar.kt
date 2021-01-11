package com.azhara.perintisadventure.entity

import com.google.firebase.Timestamp

data class BookingCar(
    var userId: String? = null,
    var carId: String? = null,
    var bookingListUserId: String? = null,
    var totalPrice: Long? = null,
    var startDate: Timestamp? = null,
    var endDate: Timestamp? = null,
    var driver: String? = null,
    var pickUpArea: String? = null,
    var statusPayment: Boolean? = false,
    var statusBooking: Int? = 0, // 0 on Progress dan 1 Selesai
    var uploadProofPayment: Boolean? = false,
    var imgUrlProofPayment: String? = null,
    var duration: Long? = null
)