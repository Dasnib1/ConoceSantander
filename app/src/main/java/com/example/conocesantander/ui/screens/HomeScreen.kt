package com.example.conocesantander.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.conocesantander.R
import com.example.conocesantander.ui.classes.GooglePlacesApi
import com.google.maps.model.PlaceType

@Composable
fun HomeScreen(){

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = "Home")
            SitiosDeSantanderList()
        }


}


@Composable
fun SitiosDeSantanderList() {
    val lugares = GooglePlacesApi.obtenerSitiosDeSantander()

    LazyColumn {
        items(lugares.size) { index ->
            val lugar = lugares[index]
            LugarCard(
                nombre = lugar.nombre,
                //fotoUrl = lugar.fotoUrl,
                valoracion = lugar.valoracion,
                direccion = lugar.direccion,
                tipo = lugar.tipo
            )        }
    }
}


@Composable
fun LugarCard(
    nombre: String,
    //fotoUrl: String?,
    valoracion: Float?,
    direccion: String,
    tipo: PlaceType
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            /* Imagen del lugar
            fotoUrl?.let { imageUrl ->
                Image(
                    painter = painterResource(R.drawable.ic_launcher_foreground), // Coloca aquí tu recurso de imagen de carga
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentScale = ContentScale.Crop
                )
            }*/

            Spacer(modifier = Modifier.height(8.dp))

            // Nombre del lugar
            Text(
                text = nombre,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Valoración del lugar
            valoracion?.let { valoracion ->
                Text(
                    text = "Valoración: $valoracion",
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Dirección del lugar
            Text(
                text = direccion,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tipo del lugar
            Text(
                text = "Tipo: ${tipo.name}",
                modifier = Modifier.fillMaxWidth(),
                color = Color.Gray
            )
        }
    }
}