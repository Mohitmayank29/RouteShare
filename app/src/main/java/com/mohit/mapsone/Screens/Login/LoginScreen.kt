package com.mohit.mapsone.Screens.Login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mohit.mapsone.R
import com.mohit.mapsone.common.CustomOutlinedTextField
import com.mohit.mapsone.common.DashboardTopBar
import com.mohit.mapsone.common.PrimaryButton
import com.mohit.mapsone.enums.TopBarType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
            rememberTopAppBarState()
        )
    // Color Palette
    val primaryBlue = Color(0xFF0077FF)
    val darkText = Color(0xFF0D1B2A)
    val backgroundGray = Color(0xFFF8FAFC)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGray)
            .imePadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 1. App Header Section
            Image(
                painter = painterResource(id = R.drawable.routeshareremovebg),
                contentDescription = "RouteShare Logo",
                modifier = Modifier.size(160.dp)
            )

            Row(
                modifier = Modifier.offset(y = (-30).dp)
            ) {
                Text(
                    text = "Route",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = darkText
                )
                Text(
                    text = "Share",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = primaryBlue
                )
            }

            Text(
                text = "Welcome back! Sign in to continue",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF64748B),
                modifier = Modifier.offset(y = (-10).dp)
            )
            Spacer(Modifier.height(4.dp))

            // 2. Elevated Card View for Inputs
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    // --- Email Field ---
                    CustomOutlinedTextField(
                        label = "Email Address",
                        placeholder = "example@domain.com",
                        value = email,
                        onValueChange = { email = it },
                        leadingIcon = Icons.Default.Email,
                        keyboardType = KeyboardType.Email
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    // --- Password Field ---
                    CustomOutlinedTextField(
                        label = "Password",
                        placeholder = "••••••••",
                        value = password,
                        onValueChange = { password = it },
                        leadingIcon = Icons.Default.Lock,
                        isPassword = true,
                        keyboardType = KeyboardType.Password
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    // Remember Me & Forgot Password
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = rememberMe,
                                onCheckedChange = { rememberMe = it },
                                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                            )
                            Text(
                                text = "Remember me",
                                fontSize = 12.sp,
                                color = darkText
                            )
                        }

                        Text(
                            text = "Forgot Password?",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryBlue,
                            modifier = Modifier.clickable {  }
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    PrimaryButton(
                        text = "Log In",
                        isLoading = isLoading,
                        onClick = { isLoading = true },
                        containerColor = MaterialTheme.colorScheme.primary
                        )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Social Login Divider
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFCBD5E1))
                Text(
                    text = "  OR  ",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFCBD5E1))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Social Action Buttons
            OutlinedButton(
                onClick = { /* Google Auth Click */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF64748B))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {

              /*  Text(
                    text = "Continue with   ",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF070707)
                )*/
                    Image(
                        painter = painterResource(id = R.drawable.googlerbg),
                        contentDescription = "Google Logo",
                        modifier = Modifier.size(90.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 5. Sign Up Link
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Don't have an account? ",
                    fontSize = 14.sp,
                    color = Color(0xFF64748B)
                )
                Text(
                    text = "Sign Up",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {  }
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewLoginScreen() {
    val navController = rememberNavController()
    LoginScreen(navController
    )
}