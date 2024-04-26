package com.example.aniframe.network

import com.example.aniframe.models.Kitsu
import java.util.Objects

data class KitsuApiResponse (
    val data: List<Kitsu> = listOf(),
    val meta: Meta,
    val links: Link
)

data class Meta(
    val count: Int
)

data class Link(
    val first: String,
    val prev: String,
    val next: String,
    val last: String
)