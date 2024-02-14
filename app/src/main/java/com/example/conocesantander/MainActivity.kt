package com.example.conocesantander

import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.compose.ConoceSantanderTheme
import com.example.conocesantander.ui.ConoceSantanderApp
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse


class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permiso otorgado, puedes iniciar la obtención de la ubicación
                startLocationUpdates()
            } else {
                // Permiso denegado, puedes mostrar un mensaje al usuario indicando que la funcionalidad no estará disponible
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
            }
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Define a variable to hold the Places API key.
        val apiKey = BuildConfig.PLACES_API_KEY

        // Log an error if apiKey is not set.
        if (apiKey.isEmpty() || apiKey == "DEFAULT_API_KEY") {
            Log.e("Places test", "No api key")
            finish()
            return
        }

        // Initialize the SDK
        Places.initializeWithNewPlacesApiEnabled(applicationContext, apiKey)

        // Create a new PlacesClient instance
        val placesClient = Places.createClient(this)

        setContent {
            var darkTheme by rememberSaveable { mutableStateOf(false) }
            var locationPermissionGranted by rememberSaveable { mutableStateOf(false) }

            ConoceSantanderTheme(darkTheme) {

                // Resto de tu UI aquí
                ConoceSantanderApp(
                    darkTheme = darkTheme,
                    onThemeUpdated = { updatedTheme ->
                        darkTheme = updatedTheme
                    },
                    placesClient = placesClient
                )
            }
        }
        checkLocationPermission()
    }
    private fun checkLocationPermission() {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Si los permisos no están otorgados, solicítalos
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            // Si los permisos ya están otorgados, inicia la obtención de la ubicación
            startLocationUpdates()
        }
    }
    private fun startLocationUpdates() {
        // Aquí puedes iniciar la obtención de la ubicación utilizando las API de ubicación de Google Play Services o el proveedor de ubicación que prefieras
        // Consulta la documentación correspondiente para más detalles
    }


}
