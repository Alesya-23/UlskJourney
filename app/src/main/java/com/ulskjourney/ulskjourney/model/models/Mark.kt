package com.ulskjourney.ulskjourney.model.models

import android.os.Parcelable

data class Mark(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val description: String
)