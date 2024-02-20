package com.example.conocesantander.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConoceSantanderViewModel : ViewModel() {
    var placeId: String? = null
    var placeName: String? = null
    var placeAddress: String? = null
    var placeRating: String? = null
    var latitude: Double? = null
    var longitude: Double? = null
    var kmFromUser: Int? = null

    var latitudeMap: Double? = null
    var longitudeMap: Double? = null
    var placeNameMap: String? = null

    var userSignIn: Boolean? = false
    var userName: String? = null
    var userId: String? = null
    var currentUser: FirebaseUser? = null
    fun setUserSignIn(isSignedIn: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                userSignIn = isSignedIn
            }
        }
    }
    fun updateUserId(id: String){
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                userId = id
            }
        }
    }
    fun updateUserName(name: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                userName = name
            }
        }
    }

    fun setPlace(id: String, name: String, address: String, rating: String, lat: Double, lng: Double, km: Int){
        viewModelScope.launch {
            placeId = id
            placeName = name
            placeAddress = address
            placeRating = rating
            latitude = lat
            longitude = lng
            kmFromUser = km
        }
    }

    fun setLocation(lat: Double, lng: Double, name: String) {
        viewModelScope.launch {
            latitudeMap = lat
            longitudeMap = lng
            placeNameMap = name
        }
    }

    fun updateCurrentUser(user: FirebaseUser?) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                currentUser = user
            }
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