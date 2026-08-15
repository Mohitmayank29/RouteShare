package com.mohit.mapsone.common

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.sp
import com.mohit.mapsone.R
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

private val PrimaryBlue = Color(0xFF0077FF)
private val CyanGlow = Color(0xFF00D4FF)
private val DarkText = Color(0xFF0D1B2A)

@Composable
fun RouteSharePremiumLoader(
    isLoading: Boolean,
    message: String = "Finding your route..."
) {
    AnimatedVisibility(
        visible = isLoading,
        enter = fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.8f),
        exit = fadeOut(animationSpec = tween(300)) + scaleOut(targetScale = 0.8f)
    ) {
        // Continuous Rotation for Outer Gradient Ring
        val infiniteTransition = rememberInfiniteTransition(label = "LoaderTransition")

        val rotationAngle by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1400, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "Rotation"
        )

        // Pulse Animation for Logo
        val pulseScale by infiniteTransition.animateFloat(
            initialValue = 0.9f,
            targetValue = 1.08f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 700, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "Pulse"
        )

        // Full Screen Semi-Transparent Dim Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.35f)), // Map ko subtly dark karega
            contentAlignment = Alignment.Center
        ) {
            // Main Loader Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .shadow(
                        elevation = 24.dp,
                        shape = RoundedCornerShape(24.dp),
                        spotColor = PrimaryBlue.copy(alpha = 0.3f)
                    ),
                shape = RoundedCornerShape(24.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp, horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Logo + Rotating Ring Box
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(90.dp)
                    ) {
                        // 1. Rotating Gradient Ring (Outer Glow)
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .rotate(rotationAngle)
                                .clip(CircleShape)
                                .border(
                                    width = 3.5.dp,
                                    brush = Brush.sweepGradient(
                                        colors = listOf(
                                            PrimaryBlue,
                                            CyanGlow,
                                            Color.Transparent,
                                            PrimaryBlue
                                        )
                                    ),
                                    shape = CircleShape
                                )
                        )

                        // 2. Inner Light Blue Circle Background
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape)
                                .background(PrimaryBlue.copy(alpha = 0.08f)),
                            contentAlignment = Alignment.Center
                        ) {
                            // 3. Logo with Pulse Effect
                            Image(
                                painter = painterResource(id = R.drawable.routeshareremovebg),
                                contentDescription = "Loader Logo",
                                modifier = Modifier
                                    .size(48.dp)
                                    .scale(pulseScale)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Dynamic Subtext
                    Text(
                        text = message,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )

                    Text(
                        text = "Please wait",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}
@Preview
@Composable
private fun previewloader() {
    RouteSharePremiumLoader(
        isLoading = true
    )

}

@Composable
fun RouteShareRippleLoader(
    isLoading: Boolean,
    message: String = "Finding your route..."
) {
    val imageList = remember {
        listOf(
            R.drawable.routeshareremovebg,
            R.drawable.building_1_svgrepo_com,
            R.drawable.map_svgrepo_com,
            R.drawable.motorcycle_svgrepo_com,
            R.drawable.road_alt_svgrepo_com,
            R.drawable.road_sign_svgrepo_com,
            R.drawable.streetlight_svgrepo_com,
            R.drawable.tree_decidious_svgrepo_com,
        )
    }

    var currentImageIndex by remember { mutableIntStateOf(0) }

    // Icon Switcher Timer
    LaunchedEffect(isLoading) {
        if (isLoading) {
            while (true) {
                delay(600.milliseconds)
                currentImageIndex = (currentImageIndex + 1) % imageList.size
            }
        }
    }

    AnimatedVisibility(
        visible = isLoading,
        enter = fadeIn(animationSpec = tween(400)),
        exit = fadeOut(animationSpec = tween(400))
    ) {
        val infiniteTransition = rememberInfiniteTransition(label = "RadarWave")

        // Continuous Expanding Wave 1
        val wave1Scale by infiniteTransition.animateFloat(
            initialValue = 0.8f,
            targetValue = 2.8f,
            animationSpec = infiniteRepeatable(
                animation = tween(1800, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "Wave1Scale"
        )
        val wave1Alpha by infiniteTransition.animateFloat(
            initialValue = 0.6f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(1800, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "Wave1Alpha"
        )

        // Wave 2 (Slight Delay feel)
        val wave2Scale by infiniteTransition.animateFloat(
            initialValue = 0.8f,
            targetValue = 2.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(1400, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "Wave2Scale"
        )
        val wave2Alpha by infiniteTransition.animateFloat(
            initialValue = 0.4f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(1400, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "Wave2Alpha"
        )

        // Center FullScreen Overlay without Popup
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.25f)), // Subtle Map Tint
            contentAlignment = Alignment.Center
        ) {

            // ==========================================
            // 1. RADAR WATER RIPPLE WAVES (BACKGROUND)
            // ==========================================
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(240.dp)
            ) {
                // Expanding Outer Blue Circle 1
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(wave1Scale)
                        .clip(CircleShape)
                        .background(PrimaryBlue.copy(alpha = wave1Alpha))
                )

                // Expanding Middle Cyan Circle 2
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(wave2Scale)
                        .clip(CircleShape)
                        .background(CyanGlow.copy(alpha = wave2Alpha))
                )

                // ==========================================
                // 2. CENTER FLOATING ICON (GLOWING CORE)
                // ==========================================
                Surface(
                    modifier = Modifier
                        .size(80.dp)
                        .shadow(16.dp, shape = CircleShape, spotColor = PrimaryBlue),
                    shape = CircleShape,
                    color = Color.White
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        AnimatedContent(
                            targetState = imageList[currentImageIndex],
                            transitionSpec = {
                                (fadeIn(animationSpec = tween(250)) + scaleIn(initialScale = 0.6f)) togetherWith
                                        (fadeOut(animationSpec = tween(250)) + scaleOut(targetScale = 0.6f))
                            },
                            label = "CenterIcon"
                        ) { targetImage ->
                            Image(
                                painter = painterResource(id = targetImage),
                                contentDescription = "Loading Radar Icon",
                                modifier = Modifier.size(44.dp)
                            )
                        }
                    }
                }
            }

            // ==========================================
            // 3. BOTTOM FLOATING TEXT PILL (NO CARD)
            // ==========================================
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = 60.dp)
                    .shadow(12.dp, shape = RoundedCornerShape(30.dp)),
                shape = RoundedCornerShape(30.dp),
                color = Color.White
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(PrimaryBlue)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = message,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewRippleLoader() {
    RouteShareRippleLoader(isLoading = true)
}