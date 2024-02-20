package com.example.conocesantander.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.conocesantander.ui.ConoceSantanderViewModel
import com.example.conocesantander.ui.classes.Favorito
import com.example.conocesantander.ui.classes.obtenerFavoritosDelUsuario
import com.google.firebase.auth.FirebaseAuth

@Composable
fun FavouriteScreen(){
    val conoceSantanderViewModel = remember { ConoceSantanderViewModel.getInstance() }
    val isSignedIn = conoceSantanderViewModel.userSignIn
        
    
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        if (isSignedIn == true){
            //TODO hacer implementar logica de favoritos
            Text(text = "Favs")
        } else{
            Text(text = "Inicia Sesión para añadir tus sitios favoritos")
        }
        /*val userId = FirebaseAuth.getInstance().currentUser?.uid

// Llama a la función para obtener los favoritos del usuario
        if (userId != null) {
            obtenerFavoritosDelUsuario(userId)
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        // El documento del usuario existe, puedes obtener los favoritos
                        val favoritos = mutableListOf<Favorito>()
                        val favoritosData = document.get("favoritos") as List<HashMap<String, String>>?
                        favoritosData?.forEach { favoritoData ->
                            val nombre = favoritoData["nombre"] ?: ""
                            val descripcion = favoritoData["descripcion"] ?: ""
                            favoritos.add(Favorito(nombre, descripcion))
                        }
                        // Hacer algo con la lista de favoritos
                        println("Favoritos del usuario: $favoritos")
                    } else {
                        // El documento del usuario no existe o está vacío
                        println("El usuario no tiene favoritos o no existe")
                    }
                }
                .addOnFailureListener { exception ->
                    // Manejar errores
                    println("Error al obtener los favoritos del usuario: $exception")
                }
        }*/
        /* Para guardar un favorito en un usuario
        * if (userId != null) {
    // Crea un objeto Favorito para agregar
    val nuevoFavorito = Favorito("Nuevo Favorito", "Descripción del nuevo favorito")

    // Llama a la función para agregar el favorito al usuario
    agregarFavoritoAlUsuario(userId, nuevoFavorito)
        .addOnSuccessListener { documentReference ->
            println("Favorito agregado con éxito, ID del documento: ${documentReference.id}")
        }
        .addOnFailureListener { exception ->
            println("Error al agregar el favorito: $exception")
        }
} else {
    println("El usuario no está autenticado")
}
        * */
    }
}