@file:OptIn(ExperimentalMaterial3Api::class)
package com.mohit.mapsone.common
import com.mohit.mapsone.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohit.mapsone.enums.TopBarType
import com.mohit.mapsone.ui.theme.NegativeRed

@Composable
fun DashboardTopBar(
    title: String,
    modifier: Modifier = Modifier,
    type: TopBarType = TopBarType.BACK_ONLY, // Default type set to BACK_ONLY
    scrollBehavior: TopAppBarScrollBehavior? = null,
    userName: String = "Anubhav",
    unreadNotificationCount: Int = 0,
    hasActiveFilter: Boolean = false,

    // Actions Lambdas (Search & Filter tabhi dikhenge jab aap pass karenge)
    onSearchClick: (() -> Unit)? = null,
    onFilterClick: (() -> Unit)? = null,
    onNotificationClick: (() -> Unit)? = null,
    onAccountClick: (() -> Unit)? = null,
    onMenuClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    // Shared Actions Composable for Search, Filter, Notifications, Account
    val renderActions: @Composable RowScope.() -> Unit = {
        // 1. Search Icon (Dikhaye jab lambda pass ho)
        if (onSearchClick != null) {
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
        }

        // 2. Filter Icon (Dikhaye jab lambda pass ho)
        if (onFilterClick != null) {
            BadgedBox(
                badge = {
                    if (hasActiveFilter) {
                        Badge(
                            containerColor = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(8.dp)
                        )
                    }
                }
            ) {
                IconButton(onClick = onFilterClick) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filter"
                    )
                }
            }
        }

        // 3. Notification Icon (If handler passed)
        if (onNotificationClick != null) {
            BadgedBox(
                badge = {
                    if (unreadNotificationCount > 0) {
                        Badge(containerColor = NegativeRed, contentColor = Color.White) {
                            Text(text = if (unreadNotificationCount > 99) "99+" else unreadNotificationCount.toString())
                        }
                    }
                }
            ) {
                IconButton(onClick = onNotificationClick) {
                    Icon(
                        painter = painterResource(R.drawable.routeshareremovebg),
                        contentDescription = "Notification",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        // 4. Account Icon (If handler passed)
        if (onAccountClick != null) {
            IconButton(onClick = onAccountClick) {
                Icon(
                    painter = painterResource(R.drawable.routeshareremovebg),
                    contentDescription = "Account",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }

    when (type) {
        TopBarType.DASHBOARD_LARGE -> {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
            ) {
                scrollBehavior?.let {
                    LargeTopAppBar(
                        scrollBehavior = it,
                        colors = TopAppBarDefaults.largeTopAppBarColors(
                            containerColor = Color.Transparent,
                            scrolledContainerColor = Color.Transparent
                        ),
                        title = {
                            val collapsedFraction = it.state.collapsedFraction
                            Column {
                                if (collapsedFraction < 0.5f) {
                                    Text(
                                        text = "Hello, $userName 👋",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Text(
                                    text = title,
                                    fontSize = 26.sp,
                                    color = NegativeRed,
                                    fontWeight = FontWeight.ExtraBold,
                                )
                                if (collapsedFraction < 0.5f) {
                                    Text(
                                        text = "BUDGET TRACKER",
                                        color = Color(0xFF888899),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                }
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = onMenuClick) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        },
                        actions = renderActions
                    )
                }
            }
        }

        TopBarType.DASHBOARD_SMALL -> {
            CenterAlignedTopAppBar(
                modifier = modifier,
                title = {
                    Text(
                        text = title,
                        fontWeight = FontWeight.SemiBold,
                        color = NegativeRed,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                actions = renderActions
            )
        }

        TopBarType.BACK_ONLY -> {
            TopAppBar(
                modifier = modifier,
                title = {
                    Text(
                        text = title,
                        fontWeight = FontWeight.SemiBold,
                        color = NegativeRed,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = renderActions
            )
        }

        TopBarType.TITLE_ONLY -> {
            TopAppBar(
                modifier = modifier,
                title = {
                    Text(
                        text = title,
                        fontWeight = FontWeight.SemiBold,
                        color = NegativeRed,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = renderActions
            )
        }
    }
}