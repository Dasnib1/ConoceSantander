package com.example.conocesantander.ui.classes

import android.provider.ContactsContract
import com.google.android.libraries.places.api.model.OpeningHours

data class Location(
    val lat: Double,
    val lng: Double
)

data class Geometry(
    val location: Location
)

data class Restaurant(
    val place_id : String,
    val name: String,
    val vicinity: String,
    val geometry: Geometry,
    val photos: List<ContactsContract.Contacts.Photo>? = null, // Lista de fotos del restaurante
    val rating: Float? = null, // Calificación del restaurante
    val openingHours: OpeningHours? = null, // Horario de apertura del restaurante
    val website: String? = null, // Sitio web del restaurante
    val phoneNumber: String? = null // Número de teléfono del restaurante
)


data class NearbySearchResponse(
    val results: List<Restaurant>
)
