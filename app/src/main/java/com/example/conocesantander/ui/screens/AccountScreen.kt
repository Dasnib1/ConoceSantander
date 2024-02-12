package com.example.conocesantander.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AccountScreen(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = "Account")
        /*
        AuthenticationManager.signIn(this, email, password,
    onSuccess = {
        // Acciones después de iniciar sesión exitosamente
        Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
    },
    onFailure = {
        // Acciones en caso de error al iniciar sesión
        Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
    }
)

AuthenticationManager.signUp(this, email, password,
    onSuccess = {
        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
    },
    onFailure = {
        // Acciones en caso de error al registrar
        Toast.makeText(this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show()
    }
)
         */
    }
}