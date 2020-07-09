package com.azhara.perintisadventure.entity

import com.google.firebase.Timestamp


data class News(
    var imgUrl: String? = null,
    var title: String? = null,
    var date: Timestamp? = null,
    var content: String? = null
)