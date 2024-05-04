package com.example.aniframe.network

import KitsuApi

data class KitsuApiResponse (
    val data: List<KitsuApi>,
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