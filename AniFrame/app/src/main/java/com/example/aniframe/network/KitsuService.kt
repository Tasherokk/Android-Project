package com.example.aniframe.network

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.aniframe.models.Kitsu
import retrofit2.Call
import retrofit2.http.GET

interface KitsuService  {
    @GET("anime")
    fun fetchKitsuList(): Call<KitsuApiResponse>

}