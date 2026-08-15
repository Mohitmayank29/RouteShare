package com.mohit.mapsone.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val title: String, val icon: ImageVector, val route: String) {
    object Maps : BottomNavItem("Maps", Icons.Default.Map, navroute.Dashboard.route)
    object CreateSession : BottomNavItem("Create", Icons.Default.AddCircle, navroute.searchdestination.route)
    object History : BottomNavItem("History", Icons.Default.History, navroute.history.route)
}
