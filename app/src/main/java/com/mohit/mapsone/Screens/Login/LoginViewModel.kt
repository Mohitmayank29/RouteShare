package com.mohit.mapsone.Screens.Login

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.mohit.mapsone.ApiResult
import com.mohit.mapsone.Constants
import com.mohit.mapsone.DataStore.PreferencesDataStore
import com.mohit.mapsone.common.SnackbarController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val preferencesDataStore: PreferencesDataStore
) : ViewModel() {

    private val TAG = "LoginViewModel"
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _loginState = MutableStateFlow<ApiResult<FirebaseUser>?>(null)
    val loginState: StateFlow<ApiResult<FirebaseUser>?> = _loginState.asStateFlow()

    fun onGoogleSignInResult(idToken: String) {
        viewModelScope.launch {
            Log.d(TAG, "Google Sign In: Received ID Token")
            _loginState.value = ApiResult.Loading()
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val result = auth.signInWithCredential(credential).await()
                val user = result.user
                if (user != null) {
                    Log.d(TAG, "Google Sign In Success: ${user.email}")
                    saveUserToFirestore(user, null)
                    saveUserToPreferences(user)
                    _loginState.value = ApiResult.Success(user)
                    SnackbarController.manager.success("Logged in with Google!")
                } else {
                    Log.e(TAG, "Google Sign In: User is null")
                    _loginState.value = ApiResult.Error("User not found")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Google Sign In Exception", e)
                val errorMsg = e.message ?: "Google Sign In Failed"
                _loginState.value = ApiResult.Error(errorMsg)
                SnackbarController.manager.error(errorMsg)
            }
        }
    }

    fun loginWithEmailPhone(email: String, phone: String = "", pass: String) {
        viewModelScope.launch {
            Log.d(TAG, "Email Login Attempt: $email")
            if (email.isEmpty() || pass.isEmpty()) {
                Log.w(TAG, "Email Login: Empty fields")
                SnackbarController.manager.warning("Please fill all fields")
                return@launch
            }
            _loginState.value = ApiResult.Loading()
            try {
                var user: FirebaseUser? = null
                try {
                    Log.d(TAG, "Attempting Sign In...")
                    val result = auth.signInWithEmailAndPassword(email, pass).await()
                    user = result.user
                } catch (e: Exception) {
                    Log.w(TAG, "Sign In Failed, attempting Sign Up: ${e.message}")
                    // Try creating account if login fails
                    val result = auth.createUserWithEmailAndPassword(email, pass).await()
                    user = result.user
                }

                if (user != null) {
                    Log.d(TAG, "Auth Success: ${user.uid}")
                    saveUserToFirestore(user, phone)
                    saveUserToPreferences(user)
                    _loginState.value = ApiResult.Success(user)
                    SnackbarController.manager.success("Login Successful!")
                } else {
                    Log.e(TAG, "Auth Failed: User is null")
                    _loginState.value = ApiResult.Error("Authentication failed")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Auth Exception", e)
                val errorMsg = e.message ?: "Authentication Failed"
                _loginState.value = ApiResult.Error(errorMsg)
                SnackbarController.manager.error(errorMsg)
            }
        }
    }

    private suspend fun saveUserToFirestore(user: FirebaseUser, phone: String?) {
        try {
            Log.d(TAG, "Saving user to Firestore: ${user.uid}")
            val userData = hashMapOf(
                "uid" to user.uid,
                "name" to (user.displayName ?: ""),
                "email" to (user.email ?: ""),
                "phone" to (phone ?: user.phoneNumber ?: ""),
                "photoUrl" to (user.photoUrl?.toString() ?: ""),
                "lastLogin" to System.currentTimeMillis()
            )
            
            firestore.collection(Constants.usertable)
                .document(user.uid)
                .set(userData)
                .await()
            Log.d(TAG, "Firestore save successful")
        } catch (e: Exception) {
            Log.e(TAG, "Firestore save failed", e)
        }
    }

    private suspend fun saveUserToPreferences(user: FirebaseUser) {
        preferencesDataStore.setLoggedIn(true)
        preferencesDataStore.setPreferenceDataStore("user_id", user.uid)
        preferencesDataStore.setPreferenceDataStore("user_email", user.email ?: "")
        preferencesDataStore.setPreferenceDataStore("user_name", user.displayName ?: "")
    }
}
