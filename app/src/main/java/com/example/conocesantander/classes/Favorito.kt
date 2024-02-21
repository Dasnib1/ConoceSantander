package com.example.conocesantander.classes

import android.util.Log
import androidx.compose.runtime.MutableState
import com.example.conocesantander.ui.ConoceSantanderViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class Favorito(
    val nombre: String,
    val descripcion: String
)

fun guardarLugarFavoritoEnFirebase(
    placeId: String,
    setEsFavorito: MutableState<Boolean>
) {
    // Verificar si el usuario ha iniciado sesión
    val conoceSantanderViewModel = ConoceSantanderViewModel.getInstance()

    val currentUser = conoceSantanderViewModel.currentUser

    if (currentUser != null) {
        val userId = conoceSantanderViewModel.userId
        val db = FirebaseFirestore.getInstance()
        val lugaresFavoritosRef = userId?.let {
            db.collection("usuarios").document(it)
                .collection("favoritos")
        }

        lugaresFavoritosRef?.add(mapOf("id" to placeId))?.addOnSuccessListener {
            Log.d("guardarLugarFavorito", "Lugar favorito agregado con éxito.")
            setEsFavorito.value = true
        }?.addOnFailureListener { e ->
            Log.e("guardarLugarFavorito", "Error al agregar el lugar favorito: $e")
            setEsFavorito.value = false
        }
    } else {
        Log.e("guardarLugarFavorito", "El usuario no ha iniciado sesión.")
        setEsFavorito.value = false
    }
}

fun eliminarLugarDeFavoritosEnFirebase(
    placeId: String,
    setEsFavorito: MutableState<Boolean>
) {
    // Verificar si el usuario ha iniciado sesión
    val conoceSantanderViewModel = ConoceSantanderViewModel.getInstance()
    val currentUser = conoceSantanderViewModel.currentUser

    if (currentUser != null) {
        val userId = conoceSantanderViewModel.userId
        val db = FirebaseFirestore.getInstance()
        val lugaresFavoritosRef = userId?.let {
            db.collection("usuarios").document(it)
                .collection("favoritos")
        }

        // Buscar el documento que contiene el lugar a eliminar
        lugaresFavoritosRef?.whereEqualTo("id", placeId)?.get()
            ?.addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    // Eliminar el documento encontrado
                    document.reference.delete()
                        .addOnSuccessListener {
                            Log.d(
                                "eliminarLugarDeFavoritos",
                                "Lugar eliminado de favoritos con éxito."
                            )
                            setEsFavorito.value = false
                        }
                        .addOnFailureListener { e ->
                            Log.e(
                                "eliminarLugarDeFavoritos",
                                "Error al eliminar el lugar de favoritos: $e"
                            )
                            setEsFavorito.value = true
                        }
                }
            }?.addOnFailureListener { e ->
            Log.e("eliminarLugarDeFavoritos", "Error al buscar el lugar de favoritos: $e")
            setEsFavorito.value = true
        }
    } else {
        Log.e("eliminarLugarDeFavoritos", "El usuario no ha iniciado sesión.")
        setEsFavorito.value = true
    }
}

fun esLugarFavoritoEnFirebase(
    placeId: String,
    esFavorito: MutableState<Boolean>
) {
    // Verificar si el usuario ha iniciado sesión
    val conoceSantanderViewModel = ConoceSantanderViewModel.getInstance()
    val currentUser = conoceSantanderViewModel.currentUser

    if (currentUser != null) {
        val userId = conoceSantanderViewModel.userId
        val db = FirebaseFirestore.getInstance()
        val lugaresFavoritosRef = userId?.let {
            db.collection("usuarios").document(it)
                .collection("favoritos")
        }

        lugaresFavoritosRef?.whereEqualTo("id", placeId)?.get()?.addOnSuccessListener { documents ->
            // Si hay algún documento que coincide con el ID del lugar, entonces está en favoritos
            esFavorito.value = !documents.isEmpty
        }?.addOnFailureListener { e ->
            Log.e("esLugarFavorito", "Error al verificar si el lugar es favorito: $e")
            esFavorito.value = false // Supongamos que no está en favoritos en caso de error
        }
    } else {
        Log.e("esLugarFavorito", "El usuario no ha iniciado sesión.")
        esFavorito.value = false // Supongamos que no está en favoritos si el usuario no ha iniciado sesión
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