package com.example.conocesantander.ui.classes

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

data class Favorito(
    val nombre: String,
    val descripcion: String
)

fun obtenerFavoritosDelUsuario(userId: String): Task<DocumentSnapshot> {
    // Obtiene una referencia a la instancia de Firestore
    val db = FirebaseFirestore.getInstance()

    // Obtiene una referencia al documento de usuario en la colección "usuarios"
    val usuarioRef = db.collection("usuarios").document(userId)

    // Retorna la tarea que obtiene el documento del usuario y sus favoritos
    return usuarioRef.get()
}
// Función para agregar favoritos a un usuario dado su ID
fun agregarFavoritoAlUsuario(userId: String, favorito: Favorito): Task<DocumentReference> {
    // Obtiene una referencia a la instancia de Firestore
    val db = FirebaseFirestore.getInstance()

    // Obtiene una referencia a la subcolección "favoritos" del usuario
    val favoritosRef = db.collection("usuarios").document(userId)
        .collection("favoritos")

    // Agrega el favorito a la subcolección "favoritos" del usuario
    return favoritosRef.add(favorito)
}