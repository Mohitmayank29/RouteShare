package com.mohit.mapsone

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import java.util.Locale

@SuppressLint("MissingPermission")
@Composable
fun MapScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var statusText by remember { mutableStateOf("Checking permissions...") }
    val cameraPositionState = rememberCameraPositionState()

    // 1. Function: Location Fetch Karein
    fun fetchLocation() {
        isLoading = true
        statusText = "Getting your current location..."

        val locationRequest = CurrentLocationRequest.Builder()
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        fusedLocationClient.getCurrentLocation(locationRequest, null)
            .addOnSuccessListener { location ->
                isLoading = false
                if (location != null) {
                    currentLocation = LatLng(location.latitude, location.longitude)
                } else {
                    statusText = "Location unavailable. Please check GPS."
                    Toast.makeText(context, "Location unavailable", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                isLoading = false
                statusText = "Failed to get location."
                Toast.makeText(context, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // 2. Launcher: GPS Turn-ON Dialog Result
    val gpsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // User ne GPS ON kar diya -> Ab automatic location fetch karo
            fetchLocation()
        } else {
            isLoading = false
            statusText = "⚠️ Please turn ON GPS to use the map."
        }
    }

    // 3. Function: Check and Request GPS Turn-ON
    fun checkAndEnableGPS() {
        isLoading = true
        statusText = "Checking GPS status..."

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 5000
        ).build()

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true)

        val client = LocationServices.getSettingsClient(context)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            // GPS pehle se ON hai -> Direct location fetch karo
            fetchLocation()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    // System dialog pop-up open karo GPS ON karne ke liye
                    val intentSenderRequest = IntentSenderRequest.Builder(exception.resolution).build()
                    gpsLauncher.launch(intentSenderRequest)
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                    isLoading = false
                }
            } else {
                isLoading = false
                statusText = "GPS is required."
            }
        }
    }

    // 4. Launcher: Permission Request Dialog
    val foregroundPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (fineGranted || coarseGranted) {
            // Permission mil gayi -> Ab GPS Check aur Auto-ON karo
            checkAndEnableGPS()
        } else {
            isLoading = false
            statusText = "❌ Location permission denied."
        }
    }

    // 5. Automatic Execution on Screen Load (NO BUTTON REQUIRED)
    LaunchedEffect(Unit) {
        val hasFine = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarse = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasFine || hasCoarse) {
            // Agar permission pehle se hai -> Direct GPS check/turn ON karo
            checkAndEnableGPS()
        } else {
            // Agar permission nahi hai -> Screen khulte hi Permission Prompt dikhao
            foregroundPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // Camera center update automatically when location arrives
    LaunchedEffect(currentLocation) {
        currentLocation?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 17f)
        }
    }

    // UI Structure (Zero Buttons)
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            if (currentLocation != null) {
                val geocoder = Geocoder(context, Locale.getDefault())

                val addresses = geocoder.getFromLocation(
                    currentLocation!!.latitude,
                    currentLocation!!.longitude,
                    1
                )

                val placeName = addresses?.firstOrNull()?.subLocality ?: "Current Location"
                // Location milne ke baad direct Google Map dikhega
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(isMyLocationEnabled = true,
                        mapType = MapType.NORMAL,
                        mapStyleOptions = null,
                        isBuildingEnabled = true,
                        isIndoorEnabled = true,
                        isTrafficEnabled = false,),
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = true,
                        compassEnabled = true,
                        myLocationButtonEnabled = true,
                        zoomGesturesEnabled = true,
                        scrollGesturesEnabled = true,
                        rotationGesturesEnabled = true,
                        tiltGesturesEnabled = true,
                        mapToolbarEnabled = true
                    ),
//                    onMapClick = { latLng ->
//                        Log.d("Map", "${latLng.latitude}, ${latLng.longitude}")
//                        Toast.makeText(context, "${latLng.latitude}, ${latLng.longitude}", Toast.LENGTH_SHORT).show()
//                    },
                    onPOIClick = { poi ->
                        Log.d("POI", poi.name)
                        Toast.makeText(context, poi.name, Toast.LENGTH_SHORT).show()
                    },
//                    onMyLocationButtonClick = {
//                        false
//                    }
                ) {
                    Marker(
                        state = rememberMarkerState(position = currentLocation!!),
                        title = placeName,
                    )
//                    Circle(
////                        center = LatLng(currentLocation!!.latitude, currentLocation!!.longitude),
////                        radius = 500.0,
////                        fillColor = MaterialTheme.colorScheme.primaryContainer,
////                        strokeColor = MaterialTheme.colorScheme.primary,
////                        strokeWidth = 1f,
//
//                    )
                }
            } else {
                // Loading ya Status screen
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}