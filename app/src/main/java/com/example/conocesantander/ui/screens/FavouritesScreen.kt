package com.example.conocesantander.ui.screens

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.conocesantander.ui.ConoceSantanderViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun FavouriteScreen(placesClient: PlacesClient){
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

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        if (isSignedIn == true) {
            if (!favoritosList.isNullOrEmpty()) {
                // Mostrar la lista de favoritos
                LazyColumn {
                    items(favoritosList) { favorito ->
                        encuentra(placesClient = placesClient, placeId =favorito )
                    }
                }

            } else {
                // Mostrar un mensaje indicando que no hay favoritos
                Text("No hay favoritos", textAlign = TextAlign.Center)
            }
        } else {
            Text("Inicia Sesión para añadir tus sitios favoritos")
        }

    }
}
fun obtenerFavoritosDeUsuario(
    successCallback: (List<String>) -> Unit,
    errorCallback: (String) -> Unit
) {
    val conoceSantanderViewModel = ConoceSantanderViewModel.getInstance()
    val currentUser = conoceSantanderViewModel.currentUser

    if (currentUser != null) {
        val userId = conoceSantanderViewModel.userId
        val db = FirebaseFirestore.getInstance()
        val lugaresFavoritosRef = userId?.let {
            db.collection("usuarios").document(it)
                .collection("favoritos")
        }

        lugaresFavoritosRef?.get()
            ?.addOnSuccessListener { documents ->
                val favoritos = mutableListOf<String>()
                for (document in documents) {
                    val lugarId = document.getString("id")
                    lugarId?.let { favoritos.add(it) }
                }
                // Llamar al callback de éxito con la lista de favoritos obtenida
                successCallback(favoritos)
            }
            ?.addOnFailureListener { e ->
                // Llamar al callback de error si ocurre un error al obtener los favoritos
                errorCallback("Error al obtener los favoritos: ${e.message}")
            }
    } else {
        // Manejar el caso en que el usuario no esté autenticado
        errorCallback("El usuario no está autenticado")
    }
}
@Composable
fun encuentra(placesClient: PlacesClient,placeId: String){
    var lugarNombre by remember { mutableStateOf("") }
    var lugarDireccion by remember { mutableStateOf("")}

    // Define a Place ID.

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