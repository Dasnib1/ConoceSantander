package com.example.conocesantander.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.compose.LocalCustomColorsPalette
import com.example.conocesantander.ui.ConoceSantanderViewModel
import com.example.conocesantander.classes.MyAppRoute
import com.example.conocesantander.classes.eliminarLugarDeFavoritosEnFirebase
import com.example.conocesantander.classes.esLugarFavoritoEnFirebase
import com.example.conocesantander.classes.guardarLugarFavoritoEnFirebase
import com.example.conocesantander.classes.obtenerFavoritosDeUsuario
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient

@Composable
fun FavouriteScreen(placesClient: PlacesClient, context: Context, navController: NavController) {
    val (favoritosList, setFavoritosList) = remember { mutableStateOf<List<String>?>(null) }
    val conoceSantanderViewModel = remember { ConoceSantanderViewModel.getInstance() }
    val isSignedIn = conoceSantanderViewModel.userSignIn
    LaunchedEffect(Unit) {
        obtenerFavoritosDeUsuario(
            successCallback = { favoritos ->
                // La lista de favoritos se ha llenado correctamente
                setFavoritosList(favoritos)
            },
            errorCallback = { errorMessage ->
                // Manejar el error si ocurre alguno al obtener los favoritos
                Log.e("Obtener favoritos", errorMessage)
            }
        )
    }


    if (isSignedIn == true) {
        if (!favoritosList.isNullOrEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Tus lugares favoritos",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                // Mostrar la lista de favoritos
                favoritosList.forEach { favorito ->
                    LugarPorId(
                        placesClient = placesClient,
                        context = context,
                        placeId = favorito,
                        navController = navController
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("No hay favoritos", textAlign = TextAlign.Center)
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Inicia Sesión para añadir tus sitios favoritos")
        }
    }


}

@Composable
fun LugarPorId(
    placesClient: PlacesClient,
    context: Context,
    placeId: String,
    navController: NavController
) {
    var placeName by remember { mutableStateOf("") }
    var placeAddress by remember { mutableStateOf("") }
    var placeType by remember { mutableStateOf("") }

    // Define a Place ID.

// Specify the fields to return.
    val placeFields =
        listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.TYPES)

// Construct a request object, passing the place ID and fields array.
    val request = FetchPlaceRequest.newInstance(placeId, placeFields)

    placesClient.fetchPlace(request)
        .addOnSuccessListener { response: FetchPlaceResponse ->
            val place = response.place
            placeName = place.name
            placeAddress = place.address
            placeType = place.placeTypes?.firstOrNull() ?: "No disponible"
        }.addOnFailureListener { exception: Exception ->
            if (exception is ApiException) {
                Log.e("Busqueda", "Place not found: ${exception.message}")
                val statusCode = exception.statusCode
                TODO("Handle error with given status code")
            }
        }

    LugarCard(
        placeName = placeName,
        placeAdress = placeAddress,
        placeId = placeId,
        placeType = placeType,
        context = context,
        navController = navController
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LugarCard(
    placeName: String,
    placeAdress: String,
    placeId: String,
    placeType: String,
    context: Context,
    navController: NavController
) {
    val typeToColorMap = mapOf(
        "restaurant" to LocalCustomColorsPalette.current.restaurant,
        "museum" to LocalCustomColorsPalette.current.museum,
        "park" to LocalCustomColorsPalette.current.park,
        "tourist_attraction" to LocalCustomColorsPalette.current.tourist_attraction
    )
    val borderColor = typeToColorMap[placeType] ?: Color.Gray


    Card(

        modifier = Modifier
            .padding(start = 12.dp, end = 12.dp)
            .height(100.dp)
            .border(width = 2.dp, color = borderColor, shape = RoundedCornerShape(8.dp))
            .fillMaxWidth(),
    ) {
        val esFavorito = remember { mutableStateOf(false) }
        fun Favorito() {
            guardarLugarFavoritoEnFirebase(placeId, esFavorito)
        }

        fun eliminarFavorito() {
            eliminarLugarDeFavoritosEnFirebase(placeId, esFavorito)
        }
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

            }
            Spacer(modifier = Modifier.width(4.dp))
            esLugarFavoritoEnFirebase(placeId, esFavorito)
            if (esFavorito.value) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorito",
                    tint = Color.Red,
                    modifier = Modifier.clickable
                        (onClick = {
                        eliminarFavorito()
                        navController.navigate(MyAppRoute.FAVOURITES)
                    })
                )
            } else {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "No favorito",
                    modifier = Modifier.clickable(onClick = { Favorito() })
                )
            }
        }
    }
}