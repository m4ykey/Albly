@file:OptIn(ExperimentalMaterial3Api::class)

package com.m4ykey.settings

import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.m4ykey.settings.theme.ThemeType
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val state = MutableStateFlow(SettingsUiState())

    private val viewModel = mockk<SettingsViewModel> {
        every { uiState } returns state
        every { effects } returns emptyFlow()
        every { onAction(any()) } just Runs
    }

    private fun setContent(onBack : () -> Unit = {}) {
        composeTestRule.setContent {
            SettingsScreen(
                onBack = onBack,
                viewModel = viewModel
            )
        }
    }

    private fun string(resId : Int) = composeTestRule.activity.getString(resId)

    @Test
    fun showsThemeLabelFromState() {
        state.value = SettingsUiState(selectedTheme = ThemeType.DARK)

        setContent()

        composeTestRule
            .onNodeWithText(string(R.string.dark_mode))
            .assertIsDisplayed()
    }

    @Test
    fun themeRowSendsOnShowThemeDialog() {
        setContent()

        composeTestRule
            .onNodeWithText(string(R.string.theme))
            .performClick()

        verify(exactly = 1) {
            viewModel.onAction(SettingsAction.OnShowThemeDialog)
        }
    }

    @Test
    fun openSourceRowSendsOnClearImageCache() {
        setContent()

        composeTestRule
            .onNodeWithText(string(R.string.open_source))
            .performClick()

        verify(exactly = 1) {
            viewModel.onAction(SettingsAction.OnOpenSource)
        }
    }

    @Test
    fun dialogIsVisibleWhenStateSaysSo() {
        state.value = SettingsUiState(isThemeDialogVisible = true)

        setContent()

        composeTestRule.onNodeWithTag("theme_dialog").assertIsDisplayed()
        composeTestRule.onNodeWithText(string(R.string.dark_mode)).assertIsDisplayed()
        composeTestRule.onNodeWithText(string(R.string.light_mode)).assertIsDisplayed()
    }

    @Test
    fun choosingThemeSendsOnChangeTheme() {
        state.value = SettingsUiState(isThemeDialogVisible = true, selectedTheme = ThemeType.DEFAULT)

        setContent()

        composeTestRule
            .onNodeWithText(string(R.string.dark_mode))
            .performClick()

        verify(exactly = 1) {
            viewModel.onAction(SettingsAction.OnChangeTheme(ThemeType.DARK))
        }
    }

    @Test
    fun backPressDismissesDialog() {
        state.value = SettingsUiState(isThemeDialogVisible = true)

        setContent()

        Espresso.pressBack()

        verify(exactly = 1) {
            viewModel.onAction(SettingsAction.OnHideThemeDialog)
        }
    }

}