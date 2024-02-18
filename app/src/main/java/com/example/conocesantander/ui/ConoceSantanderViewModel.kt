package com.example.conocesantander.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ConoceSantanderViewModel : ViewModel() {
    var placeId: String? = null
    var placeName: String? = null
    var placeAddress: String? = null
    var placeRating: String? = null
    var placePhone: String? = null
    var placeWebsite: String? = null
    var latitude: Double? = null
    var longitude: Double? = null
    var kmFromUser: Int? = null

    var userSignIn: Boolean? = false

    fun setUserSignIn(isSignedIn: Boolean){
        viewModelScope.launch {
            userSignIn = isSignedIn
        }
    }

    fun setPlace(id: String, name: String, address: String, rating: String, phone: String, website: String, lat: Double, lng: Double, km: Int){
        viewModelScope.launch {
            placeId = id
            placeName = name
            placeAddress = address
            placeRating = rating
            placePhone = phone
            placeWebsite = website
            latitude = lat
            longitude = lng
            kmFromUser = km
        }
    }

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