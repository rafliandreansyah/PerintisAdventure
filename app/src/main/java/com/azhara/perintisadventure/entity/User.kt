package com.azhara.perintisadventure.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var email: String? = null,
    var phone: String? = null,
    var imgUrl: String? = null,
    var name: String? = null
) : Parcelable