@file:OptIn(ExperimentalMaterial3Api::class)
package com.mohit.mapsone.common

import com.mohit.mapsone.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohit.mapsone.enums.TopBarType

@Composable
fun DashboardTopBar(
    title: String = "RouteShare",
    modifier: Modifier = Modifier,
    type: TopBarType = TopBarType.DASHBOARD_FLOATING,
    userName: String = "Mohit",
    unreadNotificationCount: Int = 0,

    // Actions
    onSearchClick: (() -> Unit)? = null,
    onNotificationClick: (() -> Unit)? = null,
    onAccountClick: (() -> Unit)? = null,
    onBackClick: () -> Unit = {}
) {
    val primaryBlue = Color(0xFF0077FF)
    val surfaceVariantColor = Color(0xFFF1F5F9)

    when (type) {
        TopBarType.DASHBOARD_FLOATING -> {
            Surface(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .shadow(12.dp, shape = RoundedCornerShape(28.dp)),
                shape = RoundedCornerShape(28.dp),
                color = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // 1. LEFT: APP LOGO
                    Image(
                        painter = painterResource(id = R.drawable.routeshareonlylogorbg),
                        contentDescription = "App Logo",
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // 2. CENTER: PLAIN TEXT SEARCH (No Icon, Transparent Background)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .clickable { onSearchClick?.invoke() }
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "Search destination...",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // 3. RIGHT: NOTIFICATION & PROFILE
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { onNotificationClick?.invoke() },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(surfaceVariantColor)
                        ) {
                            BadgedBox(
                                badge = {
                                    if (unreadNotificationCount > 0) {
                                        Badge(containerColor = Color.Red) {
                                            Text(
                                                text = if (unreadNotificationCount > 99) "99+" else unreadNotificationCount.toString(),
                                                color = Color.White,
                                                fontSize = 8.sp
                                            )
                                        }
                                    }
                                },
                                modifier = Modifier.size(18.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notification",
                                    tint = Color(0xFF334155),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(primaryBlue)
                                .clickable { onAccountClick?.invoke() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = userName.firstOrNull()?.toString() ?: "M",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        TopBarType.BACK_ONLY -> {
            TopAppBar(
                modifier = modifier,
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                title = {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF0D1B2A),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                }
            )
        }

        TopBarType.TITLE_ONLY -> {
            TopAppBar(
                modifier = modifier,
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                title = {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF0D1B2A),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    }
}