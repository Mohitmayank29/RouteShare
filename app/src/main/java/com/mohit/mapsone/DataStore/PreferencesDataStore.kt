package com.mohit.mapsone.DataStore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


class PreferencesDataStore @Inject constructor(@ApplicationContext private val  context: Context){

    companion object {
        // Extension property to get the DataStore instance
        val Context.dataStore by preferencesDataStore(name = "MyPrefs")

        // Preference Keys (string names only, not actual Preferences.Key)
        // 1 for Login
        const val isLogin = "isLogin"

        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")


    }
    suspend fun setLoggedIn(isLoggedIn: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = isLoggedIn
        }
    }

    // Read Login Status as Flow
    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[IS_LOGGED_IN] ?: false }

    // Direct Sync Read for Splash Screen
    suspend fun isLoggedIn(): Boolean {
        return isLoggedInFlow.first()
    }

    // Save data
    suspend fun setPreferenceDataStore(key: String, value: String) {
        context.dataStore.edit { prefs ->
            prefs[stringPreferencesKey(key)] = value
        }
    }

    // Read data
    fun getPreferenceDataStore(key: String): Flow<String> {
        return context.dataStore.data
            .map { prefs -> prefs[stringPreferencesKey(key)] ?: "" }
    }

    suspend fun getPreferenceDataString(key: String): String {
        return context.dataStore.data
            .map { prefs -> prefs[stringPreferencesKey(key)] ?: "" }
            .first()
    }

    // Clear one Time one preferences
    suspend fun removePreference(key: String) {
        context.dataStore.edit { it.remove(stringPreferencesKey(key)) }
    }

    // Clear all preferences
    suspend fun clearAllPreference() {
        context.dataStore.edit { it.clear() }
    }

}