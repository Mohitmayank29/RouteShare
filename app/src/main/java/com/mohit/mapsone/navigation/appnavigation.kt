package com.mohit.mapsone.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mohit.mapsone.Screens.dashboard.DashboardScreen
import com.mohit.mapsone.Screens.Login.LoginScreen
import com.mohit.mapsone.Screens.Splash.AnimatedSplashScreen
import com.mohit.mapsone.Screens.dashboard.CreateSessionScreen
import com.mohit.mapsone.Screens.profile.ProfileScreen
import com.mohit.mapsone.common.AppSnackbarHost
import com.mohit.mapsone.common.FloatingBottomBar

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: navroute.Splash.route
    // Bottom Bar sirf in routes par dikhega
    val showBottomBar = currentRoute in listOf(
        navroute.Dashboard.route,
        navroute.history.route,
        navroute.profile.route
    )
    Scaffold(
        snackbarHost = {
            AppSnackbarHost()
        }
    ) {paddingValues ->
        Box(modifier = Modifier.fillMaxSize()
            .padding(paddingValues)){
        NavHost(navController, startDestination = navroute.Splash.route,) {
            composable(navroute.Splash.route) {
                AnimatedSplashScreen(navController)
            }
            composable(navroute.Login.route) {
                LoginScreen(navController)
            }
            composable(navroute.Dashboard.route) {
                DashboardScreen(navController)
            }
            composable(navroute.searchdestination.route) {
                CreateSessionScreen(navController,)
            }
            composable(navroute.profile.route) {
                ProfileScreen(navController)
            }
        }
            if (showBottomBar) {
                FloatingBottomBar(
                    currentRoute = currentRoute,
                    onItemSelected = { selectedItem ->
                        if (selectedItem.route == navroute.searchdestination.route) {
                            navController.navigate(navroute.searchdestination.route)
                        } else if (currentRoute != selectedItem.route) {
                            navController.navigate(selectedItem.route) {
                                popUpTo(navroute.Dashboard.route) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}