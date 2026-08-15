package com.mohit.mapsone.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohit.mapsone.navigation.BottomNavItem

private val SelectedBg = Color(0xFFFFEBF0)
private val ActiveText = Color(0xFFE11D48)
private val InactiveText = Color(0xFF64748B)

// Mode Button Styling
private val ModeBgColor = Color(0xFF2E4F21) // Dark Green
private val ModeContentColor = Color(0xFFD2F578) // Light Green

@Composable
fun FloatingBottomBar(
    currentRoute: String,
    onItemSelected: (BottomNavItem) -> Unit,
    onModeClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem.Maps,
        BottomNavItem.CreateSession,
        BottomNavItem.History,
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 12.dp), // Screen edges ke liye padding yahan se hatai hai
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 1. LEFT MAIN FLOATING BAR (Sirf Left side padding rakhi hai)
        Surface(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp) // Left se space rahega floating look ke liye
                .shadow(16.dp, shape = RoundedCornerShape(32.dp)),
            shape = RoundedCornerShape(32.dp),
            color = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    val isSelected = currentRoute == item.route

                    val pillBgColor by animateColorAsState(
                        targetValue = if (isSelected) SelectedBg else Color.Transparent,
                        label = "PillBg"
                    )
                    val contentColor by animateColorAsState(
                        targetValue = if (isSelected) ActiveText else InactiveText,
                        label = "ContentColor"
                    )

                    Box(
                        modifier = Modifier
                            .height(50.dp)
                            .weight(1f)
                            .background(color = pillBgColor, shape = RoundedCornerShape(25.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                onItemSelected(item)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = contentColor,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = item.title,
                                fontSize = 9.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = contentColor
                            )
                        }
                    }
                }
            }
        }

        // 2. RIGHT FLOATING MODE BUTTON (Screen edge se bilkul chipka hua)
        Surface(
            modifier = Modifier
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(
                        topStart = 32.dp,
                        bottomStart = 32.dp,
                        topEnd = 0.dp,
                        bottomEnd = 0.dp
                    )
                )
                .clickable { onModeClick() },
            shape = RoundedCornerShape(
                topStart = 32.dp,
                bottomStart = 32.dp,
                topEnd = 0.dp,
                bottomEnd = 0.dp
            ),
            color = ModeBgColor
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 14.dp, end = 12.dp, top = 14.dp, bottom = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Mode Icon",
                    tint = ModeContentColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "Active",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = ModeContentColor
                    )
                }
            }
        }
    }
}