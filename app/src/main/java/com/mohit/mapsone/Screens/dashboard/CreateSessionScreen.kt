package com.mohit.mapsone.Screens.dashboard

import android.content.Context
import android.location.Geocoder
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*
import java.util.Locale

private val PrimaryBlue = Color(0xFF0077FF)
private val DarkText = Color(0xFF0D1B2A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSessionScreen(
    onBackClick: () -> Unit,
    onConfirmRoute: (start: LatLng, destination: LatLng) -> Unit
) {
    val context = LocalContext.current

    // Text Input States
    var startText by remember { mutableStateOf("My Current Location") }
    var destinationText by remember { mutableStateOf("") }

    // Coordinates States (Default coordinates - e.g. Delhi)
    var startLatLng by remember { mutableStateOf(LatLng(28.6139, 77.2090)) }
    var destinationLatLng by remember { mutableStateOf<LatLng?>(null) }

    // Map Camera State
    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(startLatLng, 13f)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // ==========================================
        // 1. Google Map View with Dynamic Markers
        // ==========================================
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = false)
        ) {
            // Start Location Marker (Blue)
            Marker(
                state = rememberMarkerState(position = startLatLng),
                title = "Start Location",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
            )

            // Destination Marker (Red) - Appears when destination is set
            destinationLatLng?.let { dest ->
                Marker(
                    state = rememberMarkerState(position = dest),
                    title = "Destination",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                )

                // Draw Direct Polyline Line between Start & Destination
                Polyline(
                    points = listOf(startLatLng, dest),
                    color = PrimaryBlue,
                    width = 12f
                )
            }
        }

        // ==========================================
        // 2. Top Input Card (Start & Destination Fields)
        // ==========================================
        Card(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                // Top Navigation Row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = DarkText
                        )
                    }
                    Text(
                        text = "Set Route",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Start Location Input Field
                OutlinedTextField(
                    value = startText,
                    onValueChange = { startText = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Start Location") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = null,
                            tint = PrimaryBlue
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Destination Location Input Field
                OutlinedTextField(
                    value = destinationText,
                    onValueChange = { destinationText = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Where to? (Destination)") },
                    placeholder = { Text("Enter destination name...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = null,
                            tint = Color.Red
                        )
                    },
                    trailingIcon = {
                        if (destinationText.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    // Search & Geocode location name into LatLng
                                    geocodeLocation(context, destinationText) { latLng ->
                                        if (latLng != null) {
                                            destinationLatLng = latLng

                                            // Auto adjust Camera to fit both markers
                                            val bounds = LatLngBounds.builder()
                                                .include(startLatLng)
                                                .include(latLng)
                                                .build()

                                            cameraPositionState.move(
                                                CameraUpdateFactory.newLatLngBounds(bounds, 100)
                                            )
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AltRoute,
                                    contentDescription = "Search Route",
                                    tint = PrimaryBlue
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }
        }

        // ==========================================
        // 3. Bottom Confirm Action Button
        // ==========================================
        if (destinationLatLng != null) {
            Button(
                onClick = {
                    onConfirmRoute(startLatLng, destinationLatLng!!)
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Text(
                    text = "CONFIRM & START SESSION",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Geocoder Helper Function: Converts Address Name (Text) to Coordinates (LatLng)
private fun geocodeLocation(
    context: Context,
    locationName: String,
    onResult: (LatLng?) -> Unit
) {
    try {
        val geocoder = Geocoder(context, Locale.getDefault())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocationName(locationName, 1) { addresses ->
                if (addresses.isNotEmpty()) {
                    val address = addresses[0]
                    onResult(LatLng(address.latitude, address.longitude))
                } else {
                    onResult(null)
                }
            }
        } else {
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocationName(locationName, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                onResult(LatLng(address.latitude, address.longitude))
            } else {
                onResult(null)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        onResult(null)
    }
}