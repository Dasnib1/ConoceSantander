package com.example.conocesantander.ui.screens

import android.annotation.SuppressLint
import android.content.ContentValues
import android.location.Location
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.conocesantander.BuildConfig
import com.example.conocesantander.R
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceLikelihood
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.tasks.await

@SuppressLint("MissingPermission")
@Composable
fun HomeScreen(placesClient: PlacesClient) {

    val context = LocalContext.current
    val fusedLocationProvider = remember { LocationServices.getFusedLocationProviderClient(context) }
    var loc: Location = Location("provider_name")
    loc.latitude = 37.7749 // Latitud de San Francisco, por ejemplo
    loc.longitude = -122.4194


    // Obtener la ubicación del usuario y actualizar la posición de la cámara
    LaunchedEffect(Unit) {
        try {
            val location = fusedLocationProvider.lastLocation.await()
            loc = location
            Log.e("Locat", location.toString())

        } catch (e: Exception) {
            Log.e("MapScreen", "Error obtaining location: ${e.message}")
        }
    }

    /*Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Home")
        encuentra(placesClient = placesClient)
    }*/

    encuentra2(placesClient = placesClient, location = loc)
    
    LazyColumn {
        item {
            RestaurantRecommendationCard()
        }
        item {
            RestaurantRecommendationCard()
        }
        // Agrega aquí otras recomendaciones como hoteles, atracciones, etc.
    }

}


@Composable
fun RestaurantRecommendationCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .background(color = Color(0xFFE6EE9C))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "Recomendaciones de Restaurantes",
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(16.dp))

                //recommendationList.forEach { recommendation ->

                    Row(modifier = Modifier.fillMaxWidth()) {
                        RestaurantCard()

                    }
                    Spacer(modifier = Modifier.height(12.dp))
                //}

                Row(modifier = Modifier.fillMaxWidth()) {
                    //recommendationList.forEach { recommendation ->
                    RestaurantCard()
                //}
                }
                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    //recommendationList.forEach { recommendation ->
                    RestaurantCard()
                //}
                }
            }
            
        }
    }
}

@Composable
fun RestaurantCard() {
    Card(
        modifier = Modifier
            .padding(end = 8.dp)
            .height(100.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
                    .clip(shape = RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "recommendation.name",
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "recommendation.address",
                    style = TextStyle(fontSize = 12.sp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "recommendation.rating".toString(),
                    style = TextStyle(fontSize = 12.sp)
                )

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

@SuppressLint("MissingPermission")
@Composable
fun encuentra2(placesClient: PlacesClient, location: Location) {
    var lugaresEncontrados by remember { mutableStateOf<List<Place>>(emptyList()) }

    // Use fields to define the data types to return.
    val placeFields: List<Place.Field> = listOf(Place.Field.NAME)

    // Use the builder to create a FindCurrentPlaceRequest.
    val request: FindCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(placeFields)

    // Set the location and radius for the request
    request.locationBias = RectangularBounds.newInstance(
        LatLng(location.latitude - 0.027, location.longitude - 0.027),
        LatLng(location.latitude + 0.027, location.longitude + 0.027)
    )

    // Limit the number of results to 3
    request.maxResults = 3

    val placeResponse = placesClient.findCurrentPlace(request)
    placeResponse.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val response = task.result
            for (placeLikelihood: PlaceLikelihood in response?.placeLikelihoods ?: emptyList()) {
                Log.i(
                    ContentValues.TAG,
                    "Place '${placeLikelihood.place.name}' has likelihood: ${placeLikelihood.likelihood}"
                )
            }
        } else {
            val exception = task.exception
            if (exception is ApiException) {
                Log.e(ContentValues.TAG, "Place not found: ${exception.statusCode}")
            }
        }
    }


        // Muestra los lugares encontrados.
        Column {
            lugaresEncontrados.forEach { lugar ->
                Text(text = lugar.name ?: "")
                Text(text = lugar.address ?: "")
                Text(text = "Valoraciones: ${lugar.userRatingsTotal}")
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }