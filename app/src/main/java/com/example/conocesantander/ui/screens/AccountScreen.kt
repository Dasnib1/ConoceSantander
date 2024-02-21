package com.example.conocesantander.ui.screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.conocesantander.ui.ConoceSantanderViewModel
import com.example.conocesantander.classes.MyAppRoute
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(navController: NavController){
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val conoceSantanderViewModel = remember { ConoceSantanderViewModel.getInstance() }
    lateinit var context: Context
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
            signUp(username, password,
                {
                    // En caso de éxito, cambia a otra pantalla
                    conoceSantanderViewModel.updateUserName(username)
                    conoceSantanderViewModel.setUserSignIn(true)
                    navController.navigate(MyAppRoute.ACCOUNT)

                },
                { errorMessage ->
                    // En caso de error, muestra un mensaje al usuario
                    }
            )
        }) {
            Text("Registro")
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = {
            signIn(username, password,
                            {
                               // En caso de éxito, cambia a otra pantalla
                                conoceSantanderViewModel.updateUserName(username)
                                conoceSantanderViewModel.setUserSignIn(true)
                                navController.navigate(MyAppRoute.ACCOUNT)
                            },
                           { errorMessage ->
                                // En caso de error, muestra un mensaje al usuario
                           }
                        )


        }) {
            Text("Inicio Sesión")
        }

    }
}
fun signUp(email: String, password: String, successCallback: () -> Unit, errorCallback: (String) -> Unit) {
    val conoceSantanderViewModel = ConoceSantanderViewModel.getInstance()
    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { signUpTask ->
            if (signUpTask.isSuccessful) {
                FirebaseAuth.getInstance().currentUser?.let {
                    conoceSantanderViewModel.updateUserId(it.uid)
                    conoceSantanderViewModel.updateCurrentUser(FirebaseAuth.getInstance().currentUser)
                }
                successCallback.invoke()
            } else {
                // Error al registrar
                val errorMessage = signUpTask.exception?.message ?: "Error desconocido al registrar"
                errorCallback.invoke(errorMessage)
            }
        }
}

fun signIn(email: String, password: String, successCallback: () -> Unit, errorCallback: (String) -> Unit) {
    val conoceSantanderViewModel =  ConoceSantanderViewModel.getInstance()

    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                FirebaseAuth.getInstance().currentUser?.let {
                    conoceSantanderViewModel.updateUserId(
                        it.uid)
                    conoceSantanderViewModel.updateCurrentUser(FirebaseAuth.getInstance().currentUser)
                }
                successCallback.invoke()
            } else {
                // Error al iniciar sesión
                errorCallback.invoke("Error al iniciar sesión: ${task.exception?.message}")
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


