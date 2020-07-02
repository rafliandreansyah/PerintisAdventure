package com.azhara.perintisadventure.entity

data class Car(
    var carId: String? = null,
    var capacity: Int? = null,
    var carName: String? = null,
    var gear: Int? = null,
    var imgUrl: String? = null,
    var luggage: Int? = null,
    var partnerId: String? = null,
    var price: Long? = null,
    var statusReady: Boolean? = false,
    var year: Int? = null,
    var booked: List<BookedDate>? = null,
    var carNumberPlate: String? = null
)