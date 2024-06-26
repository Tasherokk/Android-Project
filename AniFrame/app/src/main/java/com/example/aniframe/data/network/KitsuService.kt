package com.example.aniframe.data.network

import KitsuDetailsApi
import retrofit2.http.GET
import retrofit2.http.Query

interface KitsuService  {

    @GET("anime")
    suspend fun fetchKitsuList(
        @Query("page[limit]") limit: Int,
        @Query("page[offset]") offset: Int
    ): KitsuApiResponse
    @GET("anime")
    suspend fun fetchKitsuListByName(
        @Query("filter[text]") searchText: String): KitsuApiResponse

    @GET("anime")
    suspend fun fetchKitsuDetails(
        @Query("filter[id]") kitsuId: Int): KitsuDetailsApiResponse

    @GET("trending/anime")
    suspend fun fetchKitsuTrendingList(): KitsuApiResponse

    @GET("anime")
    suspend fun sortBy(
        @Query("sort") sortAttr: String) : KitsuApiResponse

}