package com.mohit.mapsone.Screens.dashboard

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

private val PrimaryBlue = Color(0xFF0077FF)
private val DarkText = Color(0xFF0D1B2A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSessionScreen(
    navController: NavHostController,
) {
    val context = LocalContext.current

    var startText by remember { mutableStateOf("My Current Location") }
    var destinationText by remember { mutableStateOf("") }

    var startLatLng by remember { mutableStateOf(LatLng(28.6139, 77.2090)) }
    var destinationLatLng by remember { mutableStateOf<LatLng?>(null) }

    // Suggestions List State
    var suggestions by remember { mutableStateOf<List<Address>>(emptyList()) }
    var showSuggestions by remember { mutableStateOf(false) }

    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(startLatLng, 13f)
    }

    // Live Geocoding Search trigger when user types
    LaunchedEffect(destinationText) {
        if (destinationText.length > 2 && showSuggestions) {
            fetchLocationSuggestions(context, destinationText) { addressList ->
                suggestions = addressList
            }
        } else if (destinationText.isEmpty()) {
            suggestions = emptyList()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // 1. Google Map
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = false)
        ) {
            Marker(
                state = rememberMarkerState(position = startLatLng),
                title = "Start Location",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
            )

            destinationLatLng?.let { dest ->
                Marker(
                    state = rememberMarkerState(position = dest),
                    title = "Destination",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                )

                Polyline(
                    points = listOf(startLatLng, dest),
                    color = PrimaryBlue,
                    width = 12f
                )
            }
        }

        // 2. Top Card Input & Live Suggestions
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
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

                // Start Location
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

                // Destination Search Input
                OutlinedTextField(
                    value = destinationText,
                    onValueChange = {
                        destinationText = it
                        showSuggestions = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Where to? (Destination)") },
                    placeholder = { Text("Enter area, city or place...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = null,
                            tint = Color.Red
                        )
                    },
                    trailingIcon = {
                        if (destinationText.isNotEmpty()) {
                            IconButton(onClick = {
                                destinationText = ""
                                suggestions = emptyList()
                                showSuggestions = false
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear",
                                    tint = Color.Gray
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                // --- AREA SUGGESTIONS DROPDOWN LIST ---
                AnimatedVisibility(visible = showSuggestions && suggestions.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .heightIn(max = 200.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC))
                    ) {
                        LazyColumn {
                            items(suggestions) { address ->
                                val placeName = address.getAddressLine(0) ?: "${address.locality}, ${address.adminArea}"

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            // Handle Select Suggestion
                                            destinationText = placeName
                                            destinationLatLng = LatLng(address.latitude, address.longitude)
                                            showSuggestions = false

                                            // Focus Map Camera
                                            val bounds = LatLngBounds.builder()
                                                .include(startLatLng)
                                                .include(destinationLatLng!!)
                                                .build()

                                            cameraPositionState.move(
                                                CameraUpdateFactory.newLatLngBounds(bounds, 120)
                                            )
                                        }
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Place,
                                        contentDescription = null,
                                        tint = Color.Gray,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = placeName,
                                        fontSize = 13.sp,
                                        color = DarkText,
                                        maxLines = 2
                                    )
                                }
                                HorizontalDivider(color = Color(0xFFE2E8F0))
                            }
                        }
                    }
                }
            }
        }

        // 3. Confirm Button
        if (destinationLatLng != null && !showSuggestions) {
            Button(
                onClick = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("selected_dest_lat", destinationLatLng!!.latitude)

                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("selected_dest_lng", destinationLatLng!!.longitude)

                    navController.popBackStack()
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

// Background Geocoder Function to Fetch Live Suggestions
private fun fetchLocationSuggestions(
    context: Context,
    query: String,
    onResult: (List<Address>) -> Unit
) {
    try {
        val geocoder = Geocoder(context, Locale.getDefault())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocationName(query, 5) { addresses ->
                onResult(addresses)
            }
        } else {
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocationName(query, 5)
            onResult(addresses ?: emptyList())
        }
    } catch (e: Exception) {
        e.printStackTrace()
        onResult(emptyList())
    }
}

@Preview
@Composable
private fun PreviewCreateSessionScreen() {
    CreateSessionScreen(navController = rememberNavController())
}