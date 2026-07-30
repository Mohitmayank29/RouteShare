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
import com.mohit.mapsone.R
import com.mohit.mapsone.common.PrimaryButton

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

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
                    // Email Field
                    Text(
                        text = "Email Address",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = darkText,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text("example@domain.com", color = Color.LightGray) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email Icon",
                                tint = primaryBlue
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryBlue,
                            unfocusedBorderColor = Color(0xFFE2E8F0)
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Password Field
                    Text(
                        text = "Password",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = darkText,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = { Text("••••••••", color = Color.LightGray) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Password Icon",
                                tint = primaryBlue
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle Password Visibility",
                                    tint = Color.Gray
                                )
                            }
                        },
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryBlue,
                            unfocusedBorderColor = Color(0xFFE2E8F0)
                        )
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
                                colors = CheckboxDefaults.colors(checkedColor = primaryBlue)
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
                            modifier = Modifier.clickable { onForgotPasswordClick() }
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    PrimaryButton(
                        text = "Log In",
                        isLoading = isLoading,
                        onClick = {
                            isLoading = true
                            onLoginSuccess()
                                  },

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
                border = BorderStroke(1.dp,primaryBlue),
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
                    color = primaryBlue,
                    modifier = Modifier.clickable { onNavigateToSignUp() }
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewLoginScreen() {
    LoginScreen(
        onLoginSuccess = {},
        onNavigateToSignUp = {},
        onForgotPasswordClick = {}
    )
}