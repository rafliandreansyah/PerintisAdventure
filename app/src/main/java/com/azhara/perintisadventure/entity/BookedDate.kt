package com.azhara.perintisadventure.entity

import com.google.firebase.Timestamp
import java.util.*

data class BookedDate(
    var userId: String? = null,
    var startDate: Timestamp? = null,
    var endDate: Timestamp? = null
)