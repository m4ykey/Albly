package com.m4ykey.lyrics.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.lyrics.domain.use_case.GetLyricsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LyricsViewModel(
    private val getLyricsUseCase: GetLyricsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LyricsUiState())
    val uiState = _uiState.asStateFlow()

    fun fetchLyrics(artist : String, track : String) {
        viewModelScope.launch {
            getLyricsUseCase(artist, track)
                .onStart { _uiState.update { it.copy(loading = true, error = null) } }
                .catch { e -> _uiState.update { it.copy(loading = false, error = e.message) } }
                .collect { data -> _uiState.update { it.copy(loading = false, data = data) }  }
        }
    }

}