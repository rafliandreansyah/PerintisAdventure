package com.azhara.perintisadventure.entity

data class Car(
    var capacity: Int? = null,
    var carName: String? = null,
    var gear: Int? = null,
    var imgUrl: String? = null,
    var luggage: Int? = null,
    var partnerId: String? = null,
    var price: Int? = null,
    var statusReady: Boolean? = false,
    var year: Int? = null,
    var booked: List<BookedDate>? = null
)