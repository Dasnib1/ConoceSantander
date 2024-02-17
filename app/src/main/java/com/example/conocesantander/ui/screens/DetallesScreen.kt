package com.example.conocesantander.ui.screens

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.conocesantander.ui.ConoceSantanderViewModel
import com.example.conocesantander.ui.MyAppRoute
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.tasks.await
import java.util.concurrent.ExecutionException

@Composable
fun DetallesScreen(
    placesClient: PlacesClient,
    navController: NavController,
    context: Context,
    placeId : String,
    placeName : String,
    placeAddress : String,
    placeRating : String,
    kmFromUser : String,
    placeLat : String,
    placeLng : String,
    placePhone : String,
    placeWebsite : String,
    )
{
    val conoceSantanderViewModel = remember { ConoceSantanderViewModel.getInstance() }

    conoceSantanderViewModel.setLocation(placeLat.toDouble(), placeLng.toDouble(), placeName)


    Column(
        modifier = Modifier.fillMaxSize(),
        //verticalArrangement = Arrangement.Center,
        //horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PhotoDetail(placeId = placeId, context = context)
        Column (
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        ){
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = placeName,
                style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold)
            )
            Row {
                Text(
                    text = placeRating,
                    style = TextStyle(fontSize = 28.sp)
                )
                Text(
                    text = "⭐",
                    style = TextStyle(fontSize = 24.sp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Dirección: $placeAddress"
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ){
                Button(
                    onClick = {

                        navController.navigate(MyAppRoute.MAP)
                    }
                ) {
                    Text(
                        text = "Ver en mapa"
                    )
                }
                Spacer(modifier = Modifier.width(32.dp))
                Text(
                    text = "A $kmFromUser metros"
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Datos de Contacto",
                style = TextStyle(fontSize = 20.sp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Teléfono: $placePhone",
                style = TextStyle(fontSize = 16.sp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Web: $placeWebsite",
                style = TextStyle(fontSize = 16.sp)
            )
        }
    }
}

@Composable
fun PhotoDetail(placeId: String, context: Context) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(placeId) {
        try {
            val placeFields = listOf(Place.Field.PHOTO_METADATAS)
            val placeRequest = FetchPlaceRequest.newInstance(placeId, placeFields)
            val placeResponse = Places.createClient(context).fetchPlace(placeRequest)
            val place: Place = placeResponse.await().place

            val photoMetadata = place.photoMetadatas?.get(0)

            if (photoMetadata != null) {
                val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                    .build()
                val photoResponse = Places.createClient(context).fetchPhoto(photoRequest)
                val fetchedBitmap = photoResponse.await().bitmap
                bitmap = fetchedBitmap
            }
        } catch (e: ExecutionException) {
            // Handle exceptions
            e.printStackTrace()
        } catch (e: InterruptedException) {
            // Handle exceptions
            e.printStackTrace()
        }
    }

    if (bitmap != null) {
        Image(
            bitmap = bitmap!!.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .size(300.dp),
            contentScale = ContentScale.FillWidth
        )
    }
}
