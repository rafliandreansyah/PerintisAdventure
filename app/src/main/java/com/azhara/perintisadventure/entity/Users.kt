package com.azhara.perintisadventure.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Users(
    var id: String? = null,
    var email: String? = null,
    var password: String? = null,
    var phone: String? = null,
    var imgUrl: String? = null,
    var name: String? = null
) : Parcelable