package com.example.conocesantander

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.compose.ConoceSantanderTheme
import com.example.conocesantander.ui.ConoceSantanderApp
import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var darkTheme by rememberSaveable { mutableStateOf(false) }
            var locationPermissionGranted by rememberSaveable { mutableStateOf(false) }

            ConoceSantanderTheme(darkTheme = darkTheme) {

                // Resto de tu UI aquí
                ConoceSantanderApp(
                    darkTheme = darkTheme,
                    onThemeUpdated = { updatedTheme ->
                        //darkTheme = updatedTheme
                    }
                )

                FeatureThatRequiresCameraPermission()


            }
        }
    }

    @SuppressLint("PermissionLaunchedDuringComposition")
    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    private fun FeatureThatRequiresCameraPermission() {

        // Camera permission state
        val cameraPermissionState = rememberPermissionState(
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        cameraPermissionState.launchPermissionRequest()

        }

}
