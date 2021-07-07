package me.fetsh.geekbrains.pod.ui.giphy

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val GIPHY_API_KEY = "sW1sd9Si3Mz4YAMUomDNCmJu5YHsLHW1"

interface GiphyAPI {
    @GET("gifs/random")
    fun getRandomGif(@Query("api_key") apiKey: String): Call<GiphyResponseData>
}