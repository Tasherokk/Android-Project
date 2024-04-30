package com.example.aniframe.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface KitsuService  {

    @GET("anime")
    fun fetchKitsuList(
        @Query("page[limit]") limit: Int,
        @Query("page[offset]") offset: Int
    ): Call<KitsuApiResponse>
    @GET("anime")
    fun fetchKitsuListByName(
        @Query("filter[text]") searchText: String): Call<KitsuApiResponse>

    @GET("trending/anime")
    fun fetchKitsuTrendingList(): Call<KitsuApiResponse>

    @GET("anime")
    fun sortBy(
        @Query("sort") sortAttr: String) : Call<KitsuApiResponse>

}