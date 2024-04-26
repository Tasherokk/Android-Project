package com.example.aniframe.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException

class SorulySearchImageTask() {

    suspend fun searchImage(imageFile: File): Any {
        val apiUrl = "https://api.trace.moe/search"

        val client = OkHttpClient()
        val mediaType = MediaType.get("image/jpeg")
        val requestBody = RequestBody.create(mediaType, imageFile)
        val request = Request.Builder()
            .url(apiUrl)
            .post(requestBody)
            .build()

        return withContext(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code $response")
                }
                val responseBody = response.body()?.string()
                val jsonObject = JSONObject(responseBody)
                val resultArray = jsonObject.getJSONArray("result")
                val firstResult = resultArray.getJSONObject(0)

                val anilist = firstResult.getInt("anilist")
                val filename = firstResult.getString("filename")
                val similarity = firstResult.getDouble("similarity")
                val image = firstResult.getString("image")

                val searchResult = SorulySearchResult(anilist, filename, similarity, image)
                searchResult
            } catch (e: IOException) {
                e.printStackTrace()
                "Error occurred: ${e.message}"
            }
        }
    }


}
