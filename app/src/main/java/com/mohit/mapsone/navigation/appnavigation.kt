package com.mohit.mapsone.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mohit.mapsone.Screens.Dashboard.DashboardScreen
import com.mohit.mapsone.Screens.Login.LoginScreen
import com.mohit.mapsone.Screens.Splash.AnimatedSplashScreen
import com.mohit.mapsone.common.AppSnackbarHost

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    Scaffold(
        snackbarHost = {
            AppSnackbarHost()
        }
    ) {paddingValues ->
        NavHost(navController, startDestination = navroute.Splash.route, Modifier.padding(paddingValues)) {
            composable(navroute.Splash.route) {
                AnimatedSplashScreen(navController)
            }
            composable(navroute.Login.route) {
                LoginScreen(navController)
            }
            composable(navroute.Dashboard.route) {
                DashboardScreen(navController)
            }

        }

    }
}