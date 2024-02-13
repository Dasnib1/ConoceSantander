package com.example.conocesantander.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.conocesantander.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(){
    var username by remember { mutableStateOf("yourname@email.com") }
    var password by remember { mutableStateOf("abc123") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Correo electrónico") }, // Etiqueta para el campo de correo electrónico
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp) // Espaciado uniforme
        )

        PasswordTextField(
            password = password,
            onPasswordChange = { password = it } // Aquí actualizas la variable de estado de la contraseña
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            // Lógica para el botón de registro / inicio de sesión
        }) {
            Text("Registro / Iniciar Sesión")
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(text = "-- O --", modifier = Modifier.padding(vertical = 8.dp)) // Espaciado uniforme

        Button(onClick = {
            // Lógica para el botón de inicio de sesión con credenciales guardadas
        }) {
            Text("Iniciar Sesión con Credenciales Guardadas")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(
    password: String,
    onPasswordChange: (String) -> Unit
) {
    var passwordVisibility by remember { mutableStateOf(false) }


        TextField(
            value = password,
            onValueChange = { onPasswordChange(it) },
            label = { Text("Contraseña") },
            visualTransformation = if (passwordVisibility) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { /* Acción al presionar Enter en el teclado */ }
            ),
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
        )

        // Botón de alternancia para mostrar/ocultar la contraseña
        Icon(
            if (passwordVisibility) Icons.Filled.Check else Icons.Filled.Lock,
            contentDescription = "Toggle password visibility",
            tint = Color.Gray,
            modifier = Modifier
                .padding(start = 8.dp)
                .clickable { passwordVisibility = !passwordVisibility }
        )

}






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


