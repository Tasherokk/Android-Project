package com.example.aniframe.data.network

import KitsuDetailsApi

data class KitsuDetailsApiResponse (
    val data: List<KitsuDetailsApi>,
    val meta: Meta,
    val links: Link
) {
    fun getKitsu(): KitsuDetailsApi {
        return KitsuDetailsApi(
            this.data[0].id,
            this.data[0].type,
            this.data[0].attributes,
        )
    }
}

