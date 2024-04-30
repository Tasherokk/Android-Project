package com.example.aniframe.network

import com.example.aniframe.models.SorulySearchResult
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

                val searchResults = mutableListOf<SorulySearchResult>()
                for (i in 0 until resultArray.length()) {
                    val resultObject = resultArray.getJSONObject(i)
                    val anilist = resultObject.getInt("anilist")
                    val filename = resultObject.getString("filename")
                    val similarity = resultObject.getDouble("similarity")
                    val image = resultObject.getString("image")

                    val searchResult = SorulySearchResult(anilist, filename, similarity, image)
                    searchResults.add(searchResult)
                }
                searchResults


            } catch (e: IOException) {
                e.printStackTrace()
                "Error occurred: ${e.message}"
            }
        }
    }


}
