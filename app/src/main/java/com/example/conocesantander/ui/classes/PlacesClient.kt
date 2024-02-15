package com.example.conocesantander.ui.classes

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PlacesClient {
    private const val BASE_URL = "https://maps.googleapis.com/maps/api/"

    fun create(): PlacesService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(PlacesService::class.java)
    }
}