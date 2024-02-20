package com.example.conocesantander.ui.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.conocesantander.R
import com.example.conocesantander.ui.ConoceSantanderViewModel
import com.example.conocesantander.ui.MyAppRoute

@Composable
fun UserProfileScreen(navController: NavController){
    val conoceSantanderViewModel = remember { ConoceSantanderViewModel.getInstance() }
    val (favoritosList, setFavoritosList) = remember { mutableStateOf<List<String>?>(null) }


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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        UserImage()
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = conoceSantanderViewModel.userName ?: "AAA",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp)
        )

        Spacer(modifier = Modifier.height(8.dp))
        if (favoritosList != null) {
            Text(
                text = "Nº de favoritos: " + favoritosList.size.toString(),
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                conoceSantanderViewModel.setUserSignIn(false)
                navController.navigate(MyAppRoute.ACCOUNT)
            }
        ) {
            Text(
                text = "Cerrar Sesión"
            )
        }
    }
}

@Composable
fun UserImage (){
    val rainbowColorsBrush = remember {
        Brush.sweepGradient(
            listOf(
                Color(0xFF9575CD),
                Color(0xFFBA68C8),
                Color(0xFFE57373),
                Color(0xFFFFB74D),
                Color(0xFFFFF176),
                Color(0xFFAED581),
                Color(0xFF4DD0E1),
                Color(0xFF9575CD)
            )
        )
    }
    val borderWidth = 4.dp
    Image(
        painter = painterResource(id = R.drawable.ic_launcher_foreground),
        contentDescription = ("Imagen Usuario"),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(150.dp)
            .border(
                BorderStroke(borderWidth, rainbowColorsBrush),
                CircleShape
            )
            .padding(borderWidth)
            .clip(CircleShape)
    )
}