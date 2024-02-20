package com.example.conocesantander.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.conocesantander.ui.ConoceSantanderViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.tasks.await


@SuppressLint("MissingPermission")
@Composable
fun MapScreen(
    conoceSantanderViewModel : ConoceSantanderViewModel = viewModel()
) {
    val context = LocalContext.current
    val fusedLocationProvider = remember { LocationServices.getFusedLocationProviderClient(context) }
    val cameraPositionState = rememberCameraPositionState {
        CameraPosition(LatLng(0.0, 0.0), 13f, 0f, 0f)
    }

    // Obtener la ubicación del usuario y actualizar la posición de la cámara
    LaunchedEffect(Unit) {
        try {
            val location = fusedLocationProvider.lastLocation.await()
            location?.let {
                val latLng = LatLng(location.latitude, location.longitude)
                cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 13f)
            }
            Log.e("Locat", location.toString())

        } catch (e: Exception) {
            Log.e("MapScreen", "Error obtaining location: ${e.message}")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true),
            uiSettings = MapUiSettings(),
        ){
            val conoceSantanderViewModel = remember { ConoceSantanderViewModel.getInstance() }
            val lat = conoceSantanderViewModel.latitudeMap
            val lng = conoceSantanderViewModel.longitudeMap
            val placeName = conoceSantanderViewModel.placeNameMap
            Log.e("Ayuda", "$lat $lng")


            if (lat != null && lng != null){
                Log.e("Ayuda", "Entra")
                val position = LatLng(lat, lng)
                Marker(
                    state = rememberMarkerState(position = position),
                    title = placeName,
                )
            }
        }
    }

}


