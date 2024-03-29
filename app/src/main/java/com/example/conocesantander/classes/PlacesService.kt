package com.example.conocesantander.classes

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesService {
    @GET("place/nearbysearch/json")
    fun getNearbyLugares(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("type") type: String,
        @Query("key") apiKey: String,
        @Query("language") language: String
    ): Call<NearbySearchResponse>

}