package com.mohit.mapsone.Screens.Login

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.mohit.mapsone.ApiResult
import com.mohit.mapsone.R
import com.mohit.mapsone.common.AppSnackbarHost
import com.mohit.mapsone.common.CustomOutlinedTextField
import com.mohit.mapsone.common.DashboardTopBar
import com.mohit.mapsone.common.PrimaryButton
import com.mohit.mapsone.common.SnackbarController
import com.mohit.mapsone.enums.TopBarType
import com.mohit.mapsone.navigation.navroute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    // Define local color constants to resolve "Unresolved reference" errors
    val primaryBlue = Color(0xFF0077FF)
    val darkText = Color(0xFF0D1B2A)
    val backgroundGray = Color(0xFFF8FAFC)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }
    
    val loginState by viewModel.loginState.collectAsState()
    val isLoading = loginState is ApiResult.Loading

    val context = LocalContext.current

    // Observe login success to navigate
    LaunchedEffect(loginState) {
        if (loginState is ApiResult.Success) {
            navController.navigate(navroute.Dashboard.route) {
                popUpTo(navroute.Login.route) { inclusive = true }
            }
        }
    }

    // Google Sign In Setup
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }
    val googleSignInClient = remember { GoogleSignIn.getClient(context, gso) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d("LoginScreen", "Google Sign In: Result received")
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            Log.d("LoginScreen", "Google Sign In: Success, getting token")
            account.idToken?.let { viewModel.onGoogleSignInResult(it) }
        } catch (e: ApiException) {
            Log.e("LoginScreen", "Google Sign In Failed. Code: ${e.statusCode}", e)
            SnackbarController.manager.error("Google Sign In Failed: ${e.statusCode}: ${e.message}")
        }
    }

    Scaffold(
        snackbarHost = { AppSnackbarHost(modifier = Modifier.imePadding()) },
        containerColor = backgroundGray
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                        modifier = Modifier.padding(12.dp)
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
                        Spacer(modifier = Modifier.height(8.dp))

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
                                    fontSize = 11.sp,
                                    color = darkText
                                )
                            }

                            Text(
                                text = "Forgot Password?",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryBlue,
                                modifier = Modifier.clickable { }
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        PrimaryButton(
                            text = "Log In",
                            isLoading = isLoading,
                            onClick = {
                                viewModel.loginWithEmailPhone(email = email, pass = password)
                            },
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

                // 4. Google Sign In Button
                OutlinedButton(
                    onClick = {
                        launcher.launch(googleSignInClient.signInIntent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF64748B))
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.googlerbg),
                            contentDescription = "Google Logo",
                            modifier = Modifier.size(90.dp)
                        )
                    }
                }
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