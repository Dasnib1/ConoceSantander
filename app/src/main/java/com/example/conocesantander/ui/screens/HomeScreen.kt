package com.example.conocesantander.ui.screens

import android.annotation.SuppressLint
import android.content.ContentValues
import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.conocesantander.BuildConfig
import com.example.conocesantander.ui.classes.NearbySearchResponse
import com.example.conocesantander.ui.classes.PlacesClient.create
import com.example.conocesantander.ui.classes.Restaurant
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.tasks.await
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
@Composable
fun HomeScreen(placesClient: PlacesClient) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Home")
        Encuentra3(placesClient = placesClient)
    }

}
@SuppressLint("MissingPermission")
@Composable
fun Encuentra3(placesClient: PlacesClient) {
    var restaurantesCercanos by remember { mutableStateOf<List<Restaurant>?>(null) }
    val context = LocalContext.current
    val fusedLocationProvider = remember { LocationServices.getFusedLocationProviderClient(context) }

    // Define la ubicación del usuario (latitud y longitud)
    var location: Location? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        try {
            val locationResult = fusedLocationProvider.lastLocation.await()
            location = locationResult
        } catch (e: Exception) {
            Log.e("MapScreen", "Error obtaining location: ${e.message}")
        }
    }

    // Define el radio de búsqueda en metros
    val radius = 1000

    // Define el tipo de lugar (en este caso, restaurantes)
    val type = "restaurant"

    // Realiza la llamada a la API para obtener los restaurantes cercanos cuando se obtenga la ubicación
    LaunchedEffect(location) {
        location?.let { loc ->
            fetchNearbyRestaurants(loc.latitude.toString() + "," + loc.longitude.toString(), radius, type) { nearbyRestaurants ->
                restaurantesCercanos = nearbyRestaurants.sortedByDescending { it.rating ?: Float.MIN_VALUE }
                    .take(3)

            }
        }
    }

    // Muestra los restaurantes cercanos si están disponibles
    restaurantesCercanos?.let { restaurantes ->
        if (restaurantes.isNotEmpty()) {
            Column {
                Text(text = "Restaurantes cercanos:")
                restaurantes.forEach { restaurante ->
                    Text(text = restaurante.name)
                    Text(text = "Rating: ${restaurante.rating ?: "No disponible"}")
                }
            }
        } else {
            Text(text = "No se encontraron restaurantes cercanos")
        }
    }
}
fun fetchNearbyRestaurants(
    location: String,
    radius: Int,
    type: String,
    onSuccess: (List<Restaurant>) -> Unit
) {
    val service = create()
    val apiKey = BuildConfig.PLACES_API_KEY // Reemplaza con tu clave de API de Google Places

    val call = service.getNearbyRestaurants(location, radius, type, apiKey)
    call.enqueue(object : Callback<NearbySearchResponse> {
        override fun onResponse(call: Call<NearbySearchResponse>, response: Response<NearbySearchResponse>) {
            if (response.isSuccessful) {
                val nearbyRestaurants = response.body()?.results
                nearbyRestaurants?.let {
                    onSuccess(it)
                }
            } else {
                // Maneja el error de la solicitud aquí
            }
        }

        override fun onFailure(call: Call<NearbySearchResponse>, t: Throwable) {
            // Maneja el error de la conexión aquí
        }
    })
}
@Composable
fun encuentra(placesClient: PlacesClient){
    var lugarNombre by remember { mutableStateOf("") }
    var lugarDireccion by remember { mutableStateOf("")}

    // Define a Place ID.
    val placeId = "ChIJ3S-JXmauEmsRUcIaWtf4MzE"

// Specify the fields to return.
    val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS)

// Construct a request object, passing the place ID and fields array.
    val request = FetchPlaceRequest.newInstance(placeId, placeFields)

    placesClient.fetchPlace(request)
        .addOnSuccessListener { response: FetchPlaceResponse ->
            val place = response.place
            lugarNombre = place.name
            lugarDireccion = place.address
            Log.e(ContentValues.TAG,"Place found: ${place.name}")

        }.addOnFailureListener { exception: Exception ->
            if (exception is ApiException) {
                Log.e("Busqueda", "Place not found: ${exception.message}")
                val statusCode = exception.statusCode
                TODO("Handle error with given status code")
            }
        }

    Text(text = lugarNombre)
    Text(text = lugarDireccion)


}