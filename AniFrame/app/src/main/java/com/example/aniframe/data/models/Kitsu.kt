package com.example.aniframe.data.models

import java.util.UUID

data class Kitsu(
    val id: String,
    val type: String,
    val attributes: Attributes,
    var tag: String? = null
)

data class Attributes(
    val startDate: String?,
    val canonicalTitle: String?,
    val averageRating: String?,
    val ageRating: AgeRating?,
    val posterImage: PosterImage
)
enum class Tag{
    Watching, Planning, Watched
}


