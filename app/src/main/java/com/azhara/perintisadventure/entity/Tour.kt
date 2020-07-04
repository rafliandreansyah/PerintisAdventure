package com.azhara.perintisadventure.entity

data class Tour(
    var capacity: Int? = 0,
    var durationTour: String? = null,
    var facilities: ArrayList<String>? = null,
    var imgUrl: String? = null,
    var locationTour: String? = null,
    var partnerId: String? = null,
    var price: Long? = 0,
    var timeTour: String? = null,
    var tourName: String? = null,
    var vehicle: String? = null,
    var visitedTour: ArrayList<String>? = null
)