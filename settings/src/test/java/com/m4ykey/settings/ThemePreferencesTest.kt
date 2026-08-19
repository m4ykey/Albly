package com.m4ykey.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.m4ykey.settings.theme.ThemePreferences
import com.m4ykey.settings.theme.ThemeType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class ThemePreferencesTest {

    private fun createDataStore() : Pair<DataStore<Preferences>, File> {
        val file = File.createTempFile(
            "theme_preferences_test",
            ".preferences_pb"
        )

        val datastore = PreferenceDataStoreFactory.create { file }

        return datastore to file
    }

    @Test
    fun `getSelectedThemeOption should return default when theme was not saved`() = runTest {
        val (dataStore, file) = createDataStore()

        try {
            val preferences = ThemePreferences(dataStore)

            val theme = preferences
                .getSelectedThemeOptions()
                .first()

            assertEquals(
                ThemeType.DEFAULT,
                theme
            )
        } finally {
            file.delete()
        }
    }

    @Test
    fun `saveThemeOptions should save dark selected theme`() = runTest {
        val (dataStore, file) = createDataStore()

        try {
            val preferences = ThemePreferences(dataStore)

            preferences.saveThemeOptions(ThemeType.DARK)

            val theme = preferences
                .getSelectedThemeOptions()
                .first()

            assertEquals(
                ThemeType.DARK,
                theme
            )
        } finally {
            file.delete()
        }
    }

    @Test
    fun `saveThemeOptions should save light selected theme`() = runTest {
        val (dataStore, file) = createDataStore()

        try {
            val preferences = ThemePreferences(dataStore)

            preferences.saveThemeOptions(ThemeType.LIGHT)

            val theme = preferences
                .getSelectedThemeOptions()
                .first()

            assertEquals(
                ThemeType.LIGHT,
                theme
            )
        } finally {
            file.delete()
        }
    }

    @Test
    fun `saveThemeOptions should replace previously selected theme`() = runTest {
        val (dataStore, file) = createDataStore()

        try {
            val preferences = ThemePreferences(dataStore)

            preferences.saveThemeOptions(ThemeType.LIGHT)

            assertEquals(
                ThemeType.LIGHT,
                preferences
                    .getSelectedThemeOptions()
                    .first()
            )

            preferences.saveThemeOptions(ThemeType.DARK)

            assertEquals(
                ThemeType.DARK,
                preferences
                    .getSelectedThemeOptions()
                    .first()
            )

        } finally {
            file.delete()
        }
    }

    @Test
    fun `deleteThemeOptions should reset theme to default`() = runTest {
        val (dataStore, file) = createDataStore()

        try {
            val preferences = ThemePreferences(dataStore)

            preferences.saveThemeOptions(ThemeType.LIGHT)

            assertEquals(
                ThemeType.LIGHT,
                preferences
                    .getSelectedThemeOptions()
                    .first()
            )

            preferences.deleteThemeOptions()

            assertEquals(
                ThemeType.DEFAULT,
                preferences
                    .getSelectedThemeOptions()
                    .first()
            )

        } finally {
            file.delete()
        }
    }

}