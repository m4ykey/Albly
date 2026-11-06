package com.m4ykey.lyrics.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.core.database.model.lyrics.LyricsEntity
import com.m4ykey.lyrics.data.mapper.toEntity
import com.m4ykey.lyrics.data.mapper.toLyricsItem
import com.m4ykey.lyrics.domain.model.LyricsRequest
import com.m4ykey.lyrics.domain.usecase.GetLocalLyricsUseCase
import com.m4ykey.lyrics.domain.usecase.GetLyricsStateUseCase
import com.m4ykey.lyrics.domain.usecase.GetLyricsUseCase
import com.m4ykey.lyrics.domain.usecase.ToggleLyricsSavedUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LyricsViewModel(
    private val getLyricsUseCase: GetLyricsUseCase,
    private val getLocalLyricsUseCase: GetLocalLyricsUseCase,
    private val getLyricsStateUseCase: GetLyricsStateUseCase,
    private val toggleLyricsSavedUseCase: ToggleLyricsSavedUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LyricsUiState>(LyricsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var job : Job? = null
    private var lastRequest : LyricsRequest? = null

    fun fetchLyrics(artist : String, track : String, album : String?, image : String? = null) {
        val request = LyricsRequest(artist, track, album, image)

        val alreadyHandled = request == lastRequest &&
                (_uiState.value is LyricsUiState.Success || job?.isActive == true)
        if (alreadyHandled) return

        lastRequest = request
        job?.cancel()
        job = viewModelScope.launch {
            _uiState.value = LyricsUiState.Loading

            getLyricsUseCase.invoke(track = track, album = album, artist = artist, image = image)
                .catch { e ->
                    val localLyrics = withContext(Dispatchers.IO) {
                        getLocalLyricsUseCase(track = track, artist = artist)
                    }

                    if (localLyrics != null) {
                        _uiState.value = LyricsUiState.Success(
                            item = localLyrics.toLyricsItem(),
                            isSaved = true
                        )
                    } else {
                        _uiState.value = LyricsUiState.Error(
                            e.localizedMessage ?: "An unexpected error occurred"
                        )
                    }
                }
                .collect { result ->
                    val sourceStr = result.source?.name?.lowercase().orEmpty()
                    val localState = withContext(Dispatchers.IO) {
                        getLyricsStateUseCase(
                            source = sourceStr,
                            remoteId = result.id,
                            isrc = result.isrc
                        )
                    }

                    _uiState.value = LyricsUiState.Success(
                        item = result,
                        isSaved = localState != null
                    )
                }
        }
    }

    fun toggleSave() {
        val currentState = _uiState.value
        if (currentState is LyricsUiState.Success) {
            viewModelScope.launch {
                val isCurrentlySaved = currentState.isSaved
                toggleLyricsSavedUseCase(
                    lyrics = currentState.item.toEntity(),
                    isCurrentlySaved = isCurrentlySaved
                )

                _uiState.value = currentState.copy(isSaved = !isCurrentlySaved)
            }
        }
    }
}