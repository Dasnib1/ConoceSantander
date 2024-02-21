package com.example.conocesantander.classes

import android.app.Activity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object AuthenticationManager {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signIn(activity: Activity, email: String, password: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // El usuario inició sesión exitosamente
                    onSuccess()
                } else {
                    // Error al iniciar sesión
                    onFailure()
                }
            }
    }

    fun signUp(activity: Activity, email: String, password: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // El usuario se registró exitosamente
                    onSuccess()
                } else {
                    // Error al registrar el usuario
                    onFailure()
                }
            }
    }

}
