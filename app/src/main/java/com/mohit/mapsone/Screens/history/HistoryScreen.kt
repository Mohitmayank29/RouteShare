package com.mohit.mapsone.Screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

// 1. Data Model for History Session
data class TripHistoryItem(
    val id: String,
    val destinationName: String,
    val startLocation: String,
    val date: String,
    val time: String,
    val monthYear: String, // Grouping key (e.g. "August 2026")
    val personCount: Int,
    val personLimit: Int,
    val distanceKm: Double,
    val durationMin: Int,
    val isCompleted: Boolean,
    val shareCode: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavHostController
) {
    // Dummy Data Grouped by Months
    val sampleHistory = remember {
        listOf(
            TripHistoryItem(
                id = "1",
                destinationName = "Connaught Place, New Delhi",
                startLocation = "Raj Nagar, Ghaziabad",
                date = "Aug 04, 2026",
                time = "05:30 PM",
                monthYear = "August 2026",
                personCount = 3,
                personLimit = 4,
                distanceKm = 24.5,
                durationMin = 45,
                isCompleted = true,
                shareCode = "RS-88912"
            ),
            TripHistoryItem(
                id = "2",
                destinationName = "Cyber Hub, Gurugram",
                startLocation = "Indirapuram, Ghaziabad",
                date = "Aug 01, 2026",
                time = "10:15 AM",
                monthYear = "August 2026",
                personCount = 2,
                personLimit = 2,
                distanceKm = 42.0,
                durationMin = 75,
                isCompleted = true,
                shareCode = "RS-44120"
            ),
            TripHistoryItem(
                id = "3",
                destinationName = "Sector 18 Market, Noida",
                startLocation = "Vasundhara, Ghaziabad",
                date = "Jul 28, 2026",
                time = "07:00 PM",
                monthYear = "July 2026",
                personCount = 1,
                personLimit = 3,
                distanceKm = 14.2,
                durationMin = 30,
                isCompleted = false, // Cancelled session
                shareCode = "RS-10023"
            ),
            TripHistoryItem(
                id = "4",
                destinationName = "India Gate, Delhi",
                startLocation = "Raj Nagar, Ghaziabad",
                date = "Jul 15, 2026",
                time = "04:00 PM",
                monthYear = "July 2026",
                personCount = 4,
                personLimit = 4,
                distanceKm = 28.1,
                durationMin = 50,
                isCompleted = true,
                shareCode = "RS-99812"
            )
        )
    }

    // Group items by Month & Year
    val groupedHistory = remember(sampleHistory) {
        sampleHistory.groupBy { it.monthYear }
    }

    // State for Bottom Sheet Modal (Trip Details)
    var selectedTrip by remember { mutableStateOf<TripHistoryItem?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Trip History",
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF1F5F9)
    ) { innerPadding ->

        if (sampleHistory.isEmpty()) {
            // Empty State UI
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No previous trips found",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            // History List grouped by Month
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                groupedHistory.forEach { (month, trips) ->
                    item {
                        Text(
                            text = month.uppercase(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 4.dp, top = 8.dp, bottom = 2.dp)
                        )
                    }

                    items(trips, key = { it.id }) { trip ->
                        HistoryCardItem(
                            trip = trip,
                            onClick = { selectedTrip = trip }
                        )
                    }
                }
            }
        }

        // Detailed Bottom Sheet
        if (selectedTrip != null) {
            ModalBottomSheet(
                onDismissRequest = { selectedTrip = null },
                sheetState = sheetState,
                containerColor = Color.White,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                TripDetailsBottomSheetContent(
                    trip = selectedTrip!!,
                    onClose = { selectedTrip = null }
                )
            }
        }
    }
}

// Sub-composable: List Card Item
@Composable
private fun HistoryCardItem(
    trip: TripHistoryItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Top Row: Date & Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${trip.date} • ${trip.time}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Status Badge
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (trip.isCompleted) SuccessGreen.copy(alpha = 0.1f) else CancelledRed.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = if (trip.isCompleted) "Completed" else "Cancelled",
                        color = if (trip.isCompleted) SuccessGreen else CancelledRed,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Main Destination Title
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(PrimaryBlue.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = trip.destinationName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                    Text(
                        text = "From: ${trip.startLocation}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(8.dp))

            // Bottom Info Chips: People Limit, Distance, Details Arrow
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Person Limit Badge
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = null,
                            tint = DarkText,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${trip.personCount}/${trip.personLimit} Persons",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DarkText
                        )
                    }

                    // Distance Badge
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Navigation,
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${trip.distanceKm} km",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DarkText
                        )
                    }
                }

                // Arrow Indicating Clickability
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Details",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

// Sub-composable: Trip Details Modal Bottom Sheet Content
@Composable
private fun TripDetailsBottomSheetContent(
    trip: TripHistoryItem,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 24.dp)
    ) {
        // Top Header Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Trip Summary",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )
            IconButton(onClick = onClose) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Simulated Route Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Start Location Node
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = "Start Location", fontSize = 10.sp, color = Color.Gray)
                        Text(
                            text = trip.startLocation,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DarkText
                        )
                    }
                }

                // Vertical Route Line
                Box(
                    modifier = Modifier
                        .padding(start = 8.dp, top = 4.dp, bottom = 4.dp)
                        .width(2.dp)
                        .height(20.dp)
                        .background(Color.LightGray)
                )

                // Destination Location Node
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = null,
                        tint = CancelledRed,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = "Destination", fontSize = 10.sp, color = Color.Gray)
                        Text(
                            text = trip.destinationName,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DarkText
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Grid Details
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DetailChip(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Group,
                label = "Members Joined",
                value = "${trip.personCount} / ${trip.personLimit} Persons"
            )
            DetailChip(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Timer,
                label = "Trip Duration",
                value = "${trip.durationMin} mins"
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DetailChip(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.AltRoute,
                label = "Total Distance",
                value = "${trip.distanceKm} km"
            )
            DetailChip(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Key,
                label = "Session Code",
                value = trip.shareCode
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Action Button inside Details
        Button(
            onClick = onClose,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
        ) {
            Text(text = "CLOSE DETAILS", fontWeight = FontWeight.Bold)
        }
    }
}

// Sub-composable: Small Detail Tile
@Composable
private fun DetailChip(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    value: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryBlue,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = label, fontSize = 10.sp, color = Color.Gray)
                Text(
                    text = value,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
            }
        }
    }
}

@Preview
@Composable
private fun HistoryScreenPreview() {
    HistoryScreen(navController = rememberNavController())
}