package com.example.aniframe.models

import java.util.UUID

data class Kitsu (
    val id: String,
    val type: String,
    val attributes: Attributes
)

data class Attributes(
    val createdAt: String,
    val canonicalTitle: String,
    val averageRating: String,
    val ageRating: AgeRating,
    val posterImage: PosterImage
)