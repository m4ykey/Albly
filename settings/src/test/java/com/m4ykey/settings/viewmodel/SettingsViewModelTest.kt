@file:OptIn(ExperimentalCoroutinesApi::class)

package com.m4ykey.settings.viewmodel

import app.cash.turbine.test
import com.m4ykey.settings.SettingsAction
import com.m4ykey.settings.SettingsEffect
import com.m4ykey.settings.SettingsUiState
import com.m4ykey.settings.SettingsViewModel
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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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
    fun `show theme dialog should set flag`() = runTest {
        stubPreferences()

        viewModel = SettingsViewModel(themePreferences)

        advanceUntilIdle()

        viewModel.onAction(SettingsAction.OnShowThemeDialog)

        assertTrue(viewModel.uiState.value.isThemeDialogVisible)
    }

    @Test
    fun `hide theme dialog should clear flag`() = runTest {
        stubPreferences()

        viewModel = SettingsViewModel(themePreferences)

        advanceUntilIdle()

        viewModel.onAction(SettingsAction.OnShowThemeDialog)
        viewModel.onAction(SettingsAction.OnHideThemeDialog)

        assertFalse(viewModel.uiState.value.isThemeDialogVisible)
    }

    @Test
    fun `open source should emit link effect`() = runTest {
        stubPreferences()

        viewModel = SettingsViewModel(themePreferences)

        viewModel.effects.test {
            viewModel.onAction(SettingsAction.OnOpenSource)

            advanceUntilIdle()

            assertEquals(
                SettingsEffect.OpenLink("https://github.com/m4ykey/Albly"),
                awaitItem()
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `clear image cache should emit effect`() = runTest {
        stubPreferences()

        viewModel = SettingsViewModel(themePreferences)

        viewModel.effects.test {
            viewModel.onAction(SettingsAction.OnClearImageCache)

            advanceUntilIdle()

            assertEquals(
                SettingsEffect.ClearImageCache,
                awaitItem()
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `theme load failure should emit error effect`() = runTest {
        every {
            themePreferences.getSelectedThemeOptions()
        } returns flow { throw IllegalStateException("error") }

        viewModel = SettingsViewModel(themePreferences)

        viewModel.effects.test {
            advanceUntilIdle()

            assertEquals(SettingsEffect.ShowError("error"), awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `theme load failure should keep default theme`() = runTest {
        var collected = false

        every {
            themePreferences.getSelectedThemeOptions()
        } returns flow {
            collected = true
            throw IllegalStateException("error")
        }

        viewModel = SettingsViewModel(themePreferences)

        advanceUntilIdle()

        assertTrue(collected)
        assertEquals(SettingsUiState(), viewModel.uiState.value)
    }

    @Test
    fun `uiState should start with default theme`() = runTest {
        every {
            themePreferences.getSelectedThemeOptions()
        } returns flowOf(ThemeType.DEFAULT)

        viewModel = SettingsViewModel(themePreferences)

        advanceUntilIdle()

        assertEquals(SettingsUiState(), viewModel.uiState.value)
    }

    @Test
    fun `uiState should emit selected theme from preferences`() = runTest {
        val expectedTheme = ThemeType.DARK

        every {
            themePreferences.getSelectedThemeOptions()
        } returns flowOf(expectedTheme)

        viewModel = SettingsViewModel(themePreferences)

        advanceUntilIdle()

        assertEquals(expectedTheme, viewModel.uiState.value.selectedTheme)
    }

    @Test
    fun `changing theme should save and close dialog`() = runTest {
        val expectedTheme = ThemeType.DARK

        coEvery {
            themePreferences.saveThemeOptions(expectedTheme)
        } just Runs

        every {
            themePreferences.getSelectedThemeOptions()
        } returns flowOf(ThemeType.DEFAULT)

        viewModel = SettingsViewModel(themePreferences)

        advanceUntilIdle()

        viewModel.onAction(SettingsAction.OnChangeTheme(expectedTheme))

        advanceUntilIdle()

        coVerify(exactly = 1) {
            themePreferences.saveThemeOptions(expectedTheme)
        }

        assertEquals(expectedTheme, viewModel.uiState.value.selectedTheme)
        assertFalse(viewModel.uiState.value.isThemeDialogVisible)
    }

    private fun stubPreferences(theme : ThemeType = ThemeType.DEFAULT) {
        every {
            themePreferences.getSelectedThemeOptions()
        } returns flowOf(theme)
    }

}