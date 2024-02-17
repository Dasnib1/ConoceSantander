package com.example.conocesantander.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.location.Location
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.compose.LocalCustomColorsPalette
import com.example.conocesantander.BuildConfig
import com.example.conocesantander.ui.classes.NearbySearchResponse
import com.example.conocesantander.ui.classes.PlacesClient.create
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.tasks.await
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Math.atan2
import java.lang.Math.cos
import java.lang.Math.sin
import java.lang.Math.sqrt
import java.util.concurrent.ExecutionException
import kotlin.math.roundToInt

@Composable
fun HomeScreen(placesClient: PlacesClient, context: Context, navController: NavController) {

    Column (modifier = Modifier
        .verticalScroll(rememberScrollState())
        ) {
        Row{
            Encuentra3(placeType = "restaurant", color = LocalCustomColorsPalette.current.restaurant, placeTypeName ="Restaurantes", navController)
        }/*
        Row{
            Encuentra3(placeType = "museum", color = LocalCustomColorsPalette.current.museum, placeTypeName ="Museos")
        }
        Row{
            Encuentra3(placeType = "cafe", color = LocalCustomColorsPalette.current.park, placeTypeName = "Parques")
        }*/
    }
}

@SuppressLint("MissingPermission")
@Composable
fun Encuentra3(placeType: String, color: Color,  placeTypeName: String, navController: NavController) {
    var restaurantesCercanos by remember { mutableStateOf<List<com.example.conocesantander.ui.classes.Lugar>?>(null) }
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
    val type = placeType

    // Realiza la llamada a la API para obtener los restaurantes cercanos cuando se obtenga la ubicación
    LaunchedEffect(location) {
        location?.let { loc ->
            fetchNearbyRestaurants(loc.latitude.toString() + "," + loc.longitude.toString(), radius, type) { nearbyRestaurants ->
                restaurantesCercanos = nearbyRestaurants.sortedByDescending { it.rating ?: Float.MIN_VALUE }
                    .take(1)

            }
        }
    }

    // Muestra los restaurantes cercanos si están disponibles
    restaurantesCercanos?.let { restaurantes ->
        if (restaurantes.isNotEmpty()) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(color = color)
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "Recomendaciones de " + placeTypeName,
                            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                        )
                        Spacer(modifier = Modifier.height(16.dp))


                        restaurantes.forEach { restaurante ->
                            Log.e("Place", restaurante.name)
                            Row(modifier = Modifier.fillMaxWidth()) {
                                RestaurantCard(
                                    restaurante.name,
                                    restaurante.vicinity,
                                    restaurante.rating.toString(),
                                    restaurante.place_id,
                                    restaurante.website.toString(),
                                    restaurante.phoneNumber.toString(),
                                    context,
                                    calcularDistancia(location!!.latitude,
                                        location!!.longitude,restaurante.geometry.location.lat,restaurante.geometry.location.lng
                                    ),
                                    restaurante.geometry.location.lat.toString(),
                                    restaurante.geometry.location.lng.toString(),
                                    navController
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                        }
                    }
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
    onSuccess: (List<com.example.conocesantander.ui.classes.Lugar>) -> Unit
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantCard(
    placeName: String,
    placeAdress: String,
    placeRating: String,
    placeId: String,
    placePhone: String,
    placeWebsite: String,
    context: Context,
    kmFromUser: Int,
    placeLat : String,
    placeLng : String,
    navController : NavController
) {
    Card(
        modifier = Modifier
            .padding(end = 8.dp)
            .height(100.dp)
            .fillMaxWidth(),
        onClick = {
            navController.navigate("detallesScreen/$placeId/$placeName/$placeAdress/$placeRating/$kmFromUser/$placeLat/$placeLng/$placePhone/$placeWebsite")
        }
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Photo(placeId, context)
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = placeName,
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = placeAdress,
                    style = TextStyle(fontSize = 12.sp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    Text(
                        text = placeRating + "⭐",
                        style = TextStyle(fontSize = 12.sp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = kmFromUser.toString() +" metros",
                        style = TextStyle(fontSize = 12.sp)
                    )
                }

            }
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = null,
                tint = Color.Red,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))

        }
    }
}

@Composable
fun Photo(placeId: String, context: Context) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(placeId) {
        try {
            val placeFields = listOf(Place.Field.PHOTO_METADATAS)
            val placeRequest = FetchPlaceRequest.newInstance(placeId, placeFields)
            val placeResponse = Places.createClient(context).fetchPlace(placeRequest)
            val place: Place = placeResponse.await().place

            val photoMetadata = place.photoMetadatas?.get(0)

            if (photoMetadata != null) {
                val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                    .build()
                val photoResponse = Places.createClient(context).fetchPhoto(photoRequest)
                val fetchedBitmap = photoResponse.await().bitmap
                bitmap = fetchedBitmap
            }
        } catch (e: ExecutionException) {
            // Handle exceptions
            e.printStackTrace()
        } catch (e: InterruptedException) {
            // Handle exceptions
            e.printStackTrace()
        }
    }

    if (bitmap != null) {
        Image(
            bitmap = bitmap!!.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier.size(100.dp), // Adjust size as needed
            contentScale = ContentScale.Crop
        )
    }
}

fun calcularDistancia(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Int {
    val R = 6371

    val latDistance = Math.toRadians(lat2 - lat1)
    val lonDistance = Math.toRadians(lon2 - lon1)
    val a = sin(latDistance / 2) * sin(latDistance / 2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(lonDistance / 2) * sin(lonDistance / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    val distance = R * c

    return (distance * 1000).roundToInt()
}
