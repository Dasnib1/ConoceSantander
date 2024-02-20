package com.example.conocesantander.ui.screens

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.conocesantander.ui.ConoceSantanderViewModel
import com.example.conocesantander.ui.classes.Favorito
import com.example.conocesantander.ui.classes.obtenerFavoritosDelUsuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun FavouriteScreen(){
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
                        Text(
                            text = favorito,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(8.dp)
                        )
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

