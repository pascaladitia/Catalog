package com.pascal.catalog.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

object PrefsTheme {
    private val Context.appDataStore by preferencesDataStore(name = "focus_widget")
    private val THEME_MODE_KEY = stringPreferencesKey("theme_mode")

    private fun getStore(context: Context): DataStore<Preferences> {
        return context.applicationContext.appDataStore
    }

    fun observeThemeMode(context: Context): Flow<String> =
        getStore(context).data
            .catch { e ->
                if (e is IOException) emit(emptyPreferences()) else throw e
            }
            .map { it[THEME_MODE_KEY] ?: "SYSTEM" }

    suspend fun saveThemeMode(context: Context, mode: String) {
        getStore(context).edit { it[THEME_MODE_KEY] = mode }
    }
}
