package com.example.aniframe.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SorulySearchResult(
    val anilist: Number,
    val filename: String,
    val similarity: Double,
    val image: String
) : Parcelable
