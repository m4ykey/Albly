package com.m4ykey.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.settings.theme.ThemePreferences
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val themePreferences: ThemePreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState : StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _effects = Channel<SettingsEffect>(Channel.BUFFERED)
    val effects : Flow<SettingsEffect> = _effects.receiveAsFlow()

    init {
        observeTheme()
    }

    fun onAction(action : SettingsAction) {
        when (action) {
            SettingsAction.OnHideThemeDialog -> reduce { copy(isThemeDialogVisible = false) }
            SettingsAction.OnShowThemeDialog -> reduce { copy(isThemeDialogVisible = true) }
            SettingsAction.OnOpenSource -> sendEffect(SettingsEffect.OpenLink(SOURCE_URL))
            SettingsAction.OnClearImageCache -> sendEffect(SettingsEffect.ClearImageCache)
            is SettingsAction.OnChangeTheme -> {
                reduce {
                    copy(
                        selectedTheme = action.theme,
                        isThemeDialogVisible = false
                    )
                }
                persist { themePreferences.saveThemeOptions(action.theme) }
            }
        }
    }

    private fun observeTheme() {
        viewModelScope.launch {
            themePreferences.getSelectedThemeOptions()
                .catch { exception ->
                    if (exception is CancellationException) throw exception
                    _effects.send(
                        SettingsEffect.ShowError(
                            exception.message ?: "Failed to load theme"
                        )
                    )
                }
                .collect { theme ->
                    _uiState.update { it.copy(selectedTheme = theme) }
                }
        }
    }

    private fun persist(block : suspend () -> Unit) {
        viewModelScope.launch {
            try {
                block()
            } catch (ex : CancellationException) {
                throw ex
            } catch (ex : Exception) {
                _effects.send(
                    SettingsEffect.ShowError(
                        ex.message ?: "Failed to save theme"
                    )
                )
            }
        }
    }

    private fun sendEffect(effect : SettingsEffect) {
        viewModelScope.launch { _effects.send(effect) }
    }

    private inline fun reduce(transform : SettingsUiState.() -> SettingsUiState) {
        _uiState.update { it.transform() }
    }

    private companion object {
        const val SOURCE_URL = "https://github.com/m4ykey/Albly"
    }

}