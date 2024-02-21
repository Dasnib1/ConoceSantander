package com.example.conocesantander.classes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.conocesantander.R

// Navegación
class NavigationActions(private val navController: NavController){
    fun navigateTo(destination: Screens) {
        navController.navigate(destination.route){
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true;
            }
            launchSingleTop = true;
        }
    }
}

// Clase Screens
data class Screens(
    val route: String,
    val selectedIcon: ImageVector,
    val iconTextId: Int
)

// Las 5 screens de la bottomNavBar
val DESTINATIONS= listOf(
    Screens(
        route = MyAppRoute.HOME,
        selectedIcon = Icons.Filled.Home,
        iconTextId = R.string.home
    ),
    Screens(
        route = MyAppRoute.MAP,
        selectedIcon = Icons.Filled.Place,
        iconTextId = R.string.map
    ),
    Screens(
        route = MyAppRoute.FAVOURITES,
        selectedIcon = Icons.Filled.Favorite,
        iconTextId = R.string.favourites
    ),
    Screens(
        route = MyAppRoute.ACCOUNT,
        selectedIcon = Icons.Filled.AccountCircle,
        iconTextId = R.string.account
    )
)

// Rutas de las 5 screens
object MyAppRoute {
    const val HOME = "home"
    const val MAP = "map"
    const val FAVOURITES = "favourites"
    const val ACCOUNT = "account"
}