package com.example.conocesantander

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.compose.ConoceSantanderTheme
import com.example.conocesantander.ui.ConoceSantanderApp


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var darkTheme by rememberSaveable { mutableStateOf(false)}

            ConoceSantanderTheme (darkTheme = darkTheme) {
                ConoceSantanderApp(
                    darkTheme = darkTheme,
                    onThemeUpdated = { updatedTheme ->
                        darkTheme = updatedTheme
                    }
                )
            }
        }
    }
}
