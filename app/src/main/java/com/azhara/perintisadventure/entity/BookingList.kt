package com.azhara.perintisadventure.entity

import com.google.firebase.Timestamp

data class BookingList (
    var bookingListId: String? = null,
    var bookingId: String? = null,
    var partnerId: String? = null,
    var bookingDbPartnerId: String? = null,
    var bookingName: String? = null,
    var totalPrice: Long? = null,
    var startDate: Timestamp? = null,
    var imgUrlProofPayment: String? = null,
    var bookingType: Int? = 0, // 0 Booking mobil dan 1 booking wisata
    var statusPayment: Boolean? = false,
    var uploadProofPayment: Boolean? = false
)