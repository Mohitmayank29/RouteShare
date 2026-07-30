package com.mohit.mapsone.Screens.Splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohit.mapsone.R
import kotlinx.coroutines.delay

@Composable
fun AnimatedSplashScreen(
    onSplashFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    var logoScaled by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (logoScaled) 1f else 0.3f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "LogoScaleAnimation"
    )

    LaunchedEffect(Unit) {
        logoScaled = true
        delay(2500)
        onSplashFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 1. Logo Image
            Image(
                painter = painterResource(id = R.drawable.routeshareremovebg),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(180.dp)
                    .scale(scale)
            )

            // 2. Text (Pushed upwards directly onto/touching the logo)
            AnimatedAppNameWithDelayedShimmer(
                modifier = Modifier.offset(y = (-45).dp) // Negative Y offset pulls text up completely
            )
        }
    }
}

@Composable
fun AnimatedAppNameWithDelayedShimmer(
    modifier: Modifier = Modifier
) {
    val routeText = "Route"
    val shareText = "Share"

    var visibleLettersCount by remember { mutableStateOf(0) }
    var isTypingFinished by remember { mutableStateOf(false) }

    val totalLength = routeText.length + shareText.length

    LaunchedEffect(Unit) {
        for (i in 1..totalLength) {
            delay(100)
            visibleLettersCount = i
        }
        delay(150)
        isTypingFinished = true
    }

    val transition = rememberInfiniteTransition(label = "ShimmerTransition")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = if (isTypingFinished) 1000f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ShimmerTranslate"
    )

    val routeStyle = if (isTypingFinished) {
        TextStyle(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF0D1B2A),
                    Color(0xFF2B4C7E),
                    Color(0xFF81A4CD),
                    Color(0xFF2B4C7E),
                    Color(0xFF0D1B2A)
                ),
                start = Offset(translateAnim - 400f, translateAnim - 400f),
                end = Offset(translateAnim, translateAnim)
            )
        )
    } else {
        TextStyle(color = Color(0xFF0D1B2A))
    }

    val shareStyle = if (isTypingFinished) {
        TextStyle(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF0077FF),
                    Color(0xFF00D4FF),
                    Color(0xFFFFFFFF),
                    Color(0xFF00D4FF),
                    Color(0xFF0077FF)
                ),
                start = Offset(translateAnim - 400f, translateAnim - 400f),
                end = Offset(translateAnim, translateAnim)
            )
        )
    } else {
        TextStyle(color = Color(0xFF0077FF))
    }

    val bounceEnter = fadeIn() + scaleIn(
        initialScale = 0.2f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Row(modifier = modifier) {
        // "Route"
        routeText.forEachIndexed { index, char ->
            AnimatedVisibility(
                visible = index < visibleLettersCount,
                enter = bounceEnter
            ) {
                Text(
                    text = char.toString(),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    style = routeStyle
                )
            }
        }

        // "Share"
        shareText.forEachIndexed { index, char ->
            val globalIndex = routeText.length + index
            AnimatedVisibility(
                visible = globalIndex < visibleLettersCount,
                enter = bounceEnter
            ) {
                Text(
                    text = char.toString(),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    style = shareStyle
                )
            }
        }
    }
}