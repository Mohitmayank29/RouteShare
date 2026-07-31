package com.mohit.mapsone.Screens.Splash

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import com.mohit.mapsone.DataStore.PreferencesDataStore
import com.mohit.mapsone.DataStore.PreferencesDataStore.Companion.IS_LOGGED_IN
import com.mohit.mapsone.DataStore.PreferencesDataStore.Companion.dataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

@HiltViewModel
class SplashViewModel @Inject constructor(
private  val preferencesDataStore: PreferencesDataStore,
): ViewModel() {
    suspend fun setPreferenceDataStore(key: String, value: String) {
        return preferencesDataStore.setPreferenceDataStore(key, value)
    }

    fun getPreferenceDataStore(key: String): Flow<String> {
        return preferencesDataStore.getPreferenceDataStore(key)
    }
    suspend fun setLoggedIn(isLoggedIn: Boolean) {
        preferencesDataStore.setLoggedIn(isLoggedIn)
    }

    suspend fun isLoggedIn(): Boolean {
        return preferencesDataStore.isLoggedIn()
    }
}