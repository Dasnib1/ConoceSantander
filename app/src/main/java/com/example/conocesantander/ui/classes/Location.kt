package com.example.conocesantander.ui.classes

data class Location(
    val lat: Double,
    val lng: Double
)

data class Geometry(
    val location: Location
)

data class Restaurant(
    val name: String,
    val geometry: Geometry
)

data class NearbySearchResponse(
    val results: List<Restaurant>
)
