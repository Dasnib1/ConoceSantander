package com.example.conocesantander.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.conocesantander.R
import com.example.conocesantander.ui.screens.AccountScreen
import com.example.conocesantander.ui.screens.FavouriteScreen
import com.example.conocesantander.ui.screens.HomeScreen
import com.example.conocesantander.ui.screens.MapScreen
import com.example.conocesantander.ui.screens.SearchScreen


@Composable
fun ConoceSantanderApp(darkTheme: Boolean, onThemeUpdated: (Boolean) -> Unit){

    val navController = rememberNavController()
    val navigateAction = remember(navController) {
        NavigationActions(navController)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination = navBackStackEntry?.destination?.route ?: MyAppRoute.HOME

    MyAppContent(
        navController = navController,
        selectedDestination = selectedDestination,
        navigateMyScreens = navigateAction::navigateTo,
        darkTheme = darkTheme,
        onThemeUpdated = onThemeUpdated,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    selectedDestination: String,
    navigateMyScreens: (Screens) -> Unit,
    darkTheme: Boolean,
    onThemeUpdated: (Boolean) -> Unit,
) {
    Scaffold(
        topBar = {
            ConoceSantanderTopAppBar(
                darkTheme = darkTheme,
                onThemeUpdated = onThemeUpdated
            )
        },
    ) { innerPadding ->

        Row(modifier = modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {

                NavHost(
                    modifier = Modifier
                        .weight(1f)
                        .padding(innerPadding),
                    navController = navController,
                    startDestination = MyAppRoute.HOME,
                ) {
                    composable(MyAppRoute.HOME) {
                        HomeScreen()
                    }
                    composable(MyAppRoute.MAP) {
                        MapScreen()
                    }
                    composable(MyAppRoute.SEARCH) {
                        SearchScreen()
                    }
                    composable(MyAppRoute.FAVOURITES) {
                        FavouriteScreen()
                    }
                    composable(MyAppRoute.ACCOUNT) {
                        AccountScreen()
                    }
                }

                ConoceSantanderBottomNavBar(
                    selectedDestination = selectedDestination,
                    navigateMyScreens = navigateMyScreens
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConoceSantanderTopAppBar(
    darkTheme: Boolean,
    onThemeUpdated: (Boolean) -> Unit
) {
    val iconResId = rememberSaveable { mutableStateOf(R.drawable.dark_mode_36) }
    val logo = rememberSaveable { mutableStateOf(R.drawable.conocesantander_high_resolution_logo_black_transparent) }

    TopAppBar(
        title = {
            logo.value = if (darkTheme) {
                R.drawable.conocesantander_high_resolution_logo_white_transparent
            } else {
                R.drawable.conocesantander_high_resolution_logo_black_transparent
            }

            Image(
                painter = painterResource(id = logo.value),
                contentDescription = "Logo de Conoce Santander",
                modifier = Modifier
                    .size(width = 270.dp, height = 40.dp)
                    .aspectRatio(16f / 9f)
            )
        },
        actions = {
            IconButton(onClick = {
                onThemeUpdated(!darkTheme)

                iconResId.value = if (darkTheme) {
                    R.drawable.dark_mode_36
                } else {
                    R.drawable.light_mode_36
                }
            }) {
                val xmlPainter: Painter = painterResource(iconResId.value)

                Image(
                    painter = xmlPainter,
                    contentDescription = "Toggle Theme",
                )
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(MaterialTheme.colorScheme.surfaceVariant)
    )

}


@Composable
fun ConoceSantanderBottomNavBar(
    selectedDestination: String,
    navigateMyScreens: (Screens) -> Unit
) {

    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        DESTINATIONS.forEach { destinations ->
            NavigationBarItem(
                selected = selectedDestination == destinations.route,
                onClick = { navigateMyScreens(destinations) },
                icon = {
                    Icon(
                        imageVector = destinations.selectedIcon,
                        contentDescription = stringResource(id = destinations.iconTextId),
                        modifier = Modifier.size(36.dp)
                    )
                }
            )
        }
    }

}

