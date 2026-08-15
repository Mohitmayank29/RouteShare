package com.mohit.mapsone.Screens.dashboard

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*
import com.mohit.mapsone.common.DashboardTopBar
import com.mohit.mapsone.enums.TopBarType
import com.mohit.mapsone.navigation.navroute

@Composable
fun DashboardScreen(
    navController: NavHostController,
) {
    val context = LocalContext.current

    val primaryBlue = Color(0xFF0077FF)
    val cardBgColor = Color.White
    val surfaceVariantColor = Color(0xFFF1F5F9)
    val textColor = MaterialTheme.colorScheme.onSurface
    val textSecondaryColor = Color.Gray

    var isSessionActive by remember { mutableStateOf(false) }
    var activeShareLink by remember { mutableStateOf("") }

    val defaultLocation = remember { LatLng(28.6139, 77.2090) }
    var destinationLatLng by remember { mutableStateOf<LatLng?>(null) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 14f)
    }

    val currentBackStackEntry = navController.currentBackStackEntry
    val savedStateHandle = currentBackStackEntry?.savedStateHandle

    LaunchedEffect(savedStateHandle) {
        val lat = savedStateHandle?.get<Double>("selected_dest_lat")
        val lng = savedStateHandle?.get<Double>("selected_dest_lng")

        if (lat != null && lng != null) {
            val selectedDest = LatLng(lat, lng)
            destinationLatLng = selectedDest
            isSessionActive = true

            savedStateHandle.remove<Double>("selected_dest_lat")
            savedStateHandle.remove<Double>("selected_dest_lng")

            val bounds = LatLngBounds.builder()
                .include(defaultLocation)
                .include(selectedDest)
                .build()

            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngBounds(bounds, 120)
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // 1. Fullscreen Google Map
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = false)
        ) {
            Marker(
                state = rememberMarkerState(position = defaultLocation),
                title = "My Location",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
            )
            destinationLatLng?.let { dest ->
                Marker(
                    state = rememberMarkerState(position = dest),
                    title = "Destination",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                )

                Polyline(
                    points = listOf(defaultLocation, dest),
                    color = primaryBlue,
                    width = 12f
                )
            }
        }

        // 2. Floating Top Header
        Box(
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            DashboardTopBar(
                title = "RouteShare",
                type = TopBarType.DASHBOARD_FLOATING,
                userName = "Mohit",
                unreadNotificationCount = 3,
                onSearchClick = {
                    navController.navigate(navroute.searchdestination.route)
                },
                onNotificationClick = { /* Open Notifications */ },
                onAccountClick = {
                    navController.navigate(navroute.profile.route)
                }
            )
        }

        // 3. Floating Recenter Location FAB
        FloatingActionButton(
            onClick = { /* Recenter map */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 240.dp)
                .size(44.dp),
            shape = CircleShape,
            containerColor = Color.White,
            contentColor = Color.Black
        ) {
            Icon(imageVector = Icons.Default.MyLocation, contentDescription = "My Location")
        }

        // 4. Floating Active Session Overlay (Bottom Bar ke Upar)
        AnimatedVisibility(
            visible = isSessionActive,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 90.dp) // Bottom Bar ke upar shift
                .padding(horizontal = 12.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, shape = RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = cardBgColor)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(primaryBlue.copy(alpha = 0.1f), CircleShape)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF10B981))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Session Active • Tracking On",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryBlue
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { shareTrackingLink(context, activeShareLink) },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue)
                        ) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "INVITE", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = { isSessionActive = false },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = "END SESSION", fontWeight = FontWeight.Bold, color = Color.Red, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SavedPlaceCircle(icon: ImageVector, label: String) {
    val primaryBlue = Color(0xFF0077FF)
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0F2FE)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (label == "Favorites") Color.Red else primaryBlue,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 9.sp, color = Color.Black)
    }
}

private fun shareTrackingLink(context: Context, link: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            "Track my live route on RouteShare!\nClick here to connect: $link"
        )
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Share Route Link")
    context.startActivity(shareIntent)
}

@Preview
@Composable
private fun DashboardPreview() {
    val navController = rememberNavController()
    DashboardScreen(navController)
}