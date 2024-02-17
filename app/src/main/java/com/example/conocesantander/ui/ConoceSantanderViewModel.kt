package com.example.conocesantander.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ConoceSantanderViewModel : ViewModel() {
    var latitude: Double? = null
    var longitude: Double? = null
    var placeName: String? = null

    fun setLocation(lat: Double, lng: Double, name: String) {
        viewModelScope.launch {
            latitude = lat
            longitude = lng
            placeName = name
        }
    }

    companion object {
        private var instance: ConoceSantanderViewModel? = null

        fun getInstance(): ConoceSantanderViewModel {
            return instance ?: synchronized(this) {
                instance ?: ConoceSantanderViewModel().also { instance = it }
            }
        }
    }
}