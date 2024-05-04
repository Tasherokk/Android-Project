package com.example.aniframe.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object KitsuApiClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://kitsu.io/api/edge/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val instance = retrofit.create(KitsuService:: class.java)

}