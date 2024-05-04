package com.example.aniframe.data.models

import java.util.UUID

data class Kitsu (
    val id: String,
    val type: String,
    val attributes: Attributes,
    val tag: String? = null
)


data class Attributes(
    val createdAt: String,
    val canonicalTitle: String,
    val averageRating: String,
    val ageRating: AgeRating,
    val posterImage: PosterImage
)