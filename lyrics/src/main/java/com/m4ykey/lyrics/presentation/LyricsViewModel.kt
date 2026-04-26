package com.m4ykey.lyrics.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.lyrics.domain.use_case.GetLyricsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class LyricsViewModel(
    private val getLyricsUseCase: GetLyricsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LyricsUiState>(LyricsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun fetchLyrics(artist : String, track : String) {
        viewModelScope.launch {
            _uiState.value = LyricsUiState.Loading

            getLyricsUseCase.invoke(artist, track)
                .catch { e ->
                    _uiState.value = LyricsUiState.Error(
                        e.localizedMessage ?: "An unexpected error occurred"
                    )
                }
                .collect { result ->
                    _uiState.value = LyricsUiState.Success(
                        item = result
                    )
                }
        }
    }
}