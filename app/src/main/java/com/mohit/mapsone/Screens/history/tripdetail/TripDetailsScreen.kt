package com.mohit.mapsone.Screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

private val PrimaryBlue = Color(0xFF0077FF)
private val DarkText = Color(0xFF0D1B2A)
private val SuccessGreen = Color(0xFF10B981)
private val CancelledRed = Color(0xFFEF4444)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailsScreen(
    navController: NavHostController,
    tripId: String? = "1" // Navigation argument se id pass kar sakte ho
) {
    // Mock Full Data (Real app me ViewModel se tripId se load hoga)
    val startLocation = "Raj Nagar, Ghaziabad"
    val destinationName = "Connaught Place, New Delhi"
    val date = "Aug 04, 2026"
    val startTime = "05:30 PM"
    val endTime = "06:15 PM"
    val personCount = 3
    val personLimit = 4
    val distanceKm = 24.5
    val durationMin = 45
    val isCompleted = true
    val shareCode = "RS-88912"
    val avgSpeed = "32.6 km/h"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Trip Summary",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = DarkText
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = DarkText
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Share functionality */ }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = PrimaryBlue
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF1F5F9)
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ==========================================
            // 1. Status & Session Code Header Card
            // ==========================================
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(2.dp, shape = RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "SESSION CODE", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Text(
                            text = shareCode,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = PrimaryBlue
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isCompleted) SuccessGreen.copy(alpha = 0.1f) else CancelledRed.copy(alpha = 0.1f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(if (isCompleted) SuccessGreen else CancelledRed)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isCompleted) "Completed" else "Cancelled",
                                color = if (isCompleted) SuccessGreen else CancelledRed,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ==========================================
            // 2. Full Route Path Card
            // ==========================================
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ROUTE DETAILS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Start Node
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(PrimaryBlue.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = "Start Location ($startTime)", fontSize = 11.sp, color = Color.Gray)
                            Text(
                                text = startLocation,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkText
                            )
                        }
                    }

                    // Connecting Line
                    Box(
                        modifier = Modifier
                            .padding(start = 15.dp, top = 4.dp, bottom = 4.dp)
                            .width(2.dp)
                            .height(28.dp)
                            .background(PrimaryBlue.copy(alpha = 0.4f))
                    )

                    // Destination Node
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(CancelledRed.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = null,
                                tint = CancelledRed,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = "Destination ($endTime)", fontSize = 11.sp, color = Color.Gray)
                            Text(
                                text = destinationName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkText
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ==========================================
            // 3. Performance Metrics Grid (4 Stat Cards)
            // ==========================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.AltRoute,
                    label = "Distance",
                    value = "$distanceKm km"
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Timer,
                    label = "Duration",
                    value = "$durationMin mins"
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Speed,
                    label = "Avg Speed",
                    value = avgSpeed
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.CalendarToday,
                    label = "Date",
                    value = date
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ==========================================
            // 4. Joined Members Section
            // ==========================================
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PASSENGERS / MEMBERS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                        Text(
                            text = "$personCount / $personLimit Joined",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Member 1 (Host)
                    PassengerTile(name = "Mohit Kumar (You)", role = "Host / Driver")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFF1F5F9))
                    PassengerTile(name = "Aman Sharma", role = "Passenger")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFF1F5F9))
                    PassengerTile(name = "Rohan Verma", role = "Passenger")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ==========================================
            // 5. Action Buttons
            // ==========================================
            Button(
                onClick = {
                    // Action to re-create or start same route
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Icon(imageVector = Icons.Default.Navigation, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "START SAME ROUTE AGAIN",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// Sub-composable for Stat Grid Tile
@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    value: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(PrimaryBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = label, fontSize = 10.sp, color = Color.Gray)
                Text(
                    text = value,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
            }
        }
    }
}

// Sub-composable for Member Row
@Composable
private fun PassengerTile(name: String, role: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0F2FE)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = PrimaryBlue,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = name,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkText
            )
            Text(
                text = role,
                fontSize = 10.sp,
                color = Color.Gray
            )
        }
    }
}

@Preview
@Composable
private fun TripDetailsPreview() {
    TripDetailsScreen(navController = rememberNavController())
}