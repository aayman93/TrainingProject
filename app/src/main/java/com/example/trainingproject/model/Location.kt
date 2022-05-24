package com.example.trainingproject.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Location(
    val address: String = "",
    val id: Int = -1,
    val latitude: @RawValue Any? = null,
    val longitude: @RawValue Any? = null,
    val name: String = "",
    val photo: String = ""
) : Parcelable