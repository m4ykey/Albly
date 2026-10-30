@file:OptIn(ExperimentalCoroutinesApi::class)

package com.m4ykey.settings

import app.cash.turbine.test
import com.m4ykey.settings.theme.ThemePreferences
import com.m4ykey.settings.theme.ThemeType
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class SettingsViewModelTest {

    private lateinit var themePreferences: ThemePreferences
    private lateinit var viewModel: SettingsViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        themePreferences = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `selectedTheme should emit default theme initially`() = runTest {
        every {
            themePreferences.getSelectedThemeOptions()
        } returns flowOf(ThemeType.DEFAULT)

        viewModel = SettingsViewModel(themePreferences)

        assertEquals(ThemeType.DEFAULT, viewModel.selectedTheme.value)
    }

    @Test
    fun `selectedTheme should emit selected theme from preferences`() = runTest {
        val expectedTheme = ThemeType.DARK

        every {
            themePreferences.getSelectedThemeOptions()
        } returns flowOf(expectedTheme)

        viewModel = SettingsViewModel(themePreferences)

        viewModel.selectedTheme.test {
            assertEquals(ThemeType.DEFAULT, awaitItem())
            assertEquals(expectedTheme, awaitItem())
        }
    }

    @Test
    fun `changeTheme should save selected theme`() = runTest {
        val expectedTheme = ThemeType.DARK

        coEvery {
            themePreferences.saveThemeOptions(expectedTheme)
        } just Runs

        every {
            themePreferences.getSelectedThemeOptions()
        } returns flowOf(ThemeType.DEFAULT)

        viewModel = SettingsViewModel(themePreferences)

        viewModel.changeTheme(expectedTheme)

        advanceUntilIdle()

        coVerify(exactly = 1) {
            themePreferences.saveThemeOptions(expectedTheme)
        }
    }

}