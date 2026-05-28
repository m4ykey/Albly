package com.m4ykey.settings.theme

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import com.m4ykey.core.ext.safeDataStoreOperations
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class ThemePreferences(
    private val dataStore : DataStore<Preferences>
) {

    companion object {
        private val SELECTED_THEME_KEY = intPreferencesKey("selected_theme")
    }

    suspend fun saveThemeOptions(theme : ThemeType) {
        safeDataStoreOperations {
            dataStore.edit { pref ->
                pref[SELECTED_THEME_KEY] = theme.index
            }
        }
    }

    suspend fun deleteThemeOptions() {
        safeDataStoreOperations {
            dataStore.edit { pref ->
                pref.remove(SELECTED_THEME_KEY)
            }
        }
    }

    fun getSelectedThemeOptions() : Flow<ThemeType> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { pref ->
                val index = pref[SELECTED_THEME_KEY] ?: ThemeType.DEFAULT.index
                ThemeType.fromIndex(index)
            }
    }

}