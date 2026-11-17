package com.m4ykey.lyrics.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.lyrics.data.mapper.toEntity
import com.m4ykey.lyrics.data.mapper.toLyricsItem
import com.m4ykey.lyrics.domain.model.LyricsItem
import com.m4ykey.lyrics.domain.model.LyricsRequest
import com.m4ykey.lyrics.domain.usecase.CacheLyricsUseCase
import com.m4ykey.lyrics.domain.usecase.GetLyricsSavedStateUseCase
import com.m4ykey.lyrics.domain.usecase.GetLyricsStateUseCase
import com.m4ykey.lyrics.domain.usecase.GetLyricsUseCase
import com.m4ykey.lyrics.domain.usecase.ToggleLyricsSavedUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LyricsViewModel(
    private val getLyricsUseCase: GetLyricsUseCase,
    private val getLyricsStateUseCase: GetLyricsStateUseCase,
    private val toggleLyricsSavedUseCase: ToggleLyricsSavedUseCase,
    private val getLyricsSavedStateUseCase : GetLyricsSavedStateUseCase,
    private val cacheLyricsUseCase : CacheLyricsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LyricsUiState>(LyricsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _effects = Channel<LyricsEffect>(Channel.BUFFERED)
    val effects : Flow<LyricsEffect> = _effects.receiveAsFlow()

    private var job : Job? = null
    private var lastRequest : LyricsRequest? = null

    fun onAction(action: LyricsAction) {
        when (action) {
            is LyricsAction.OnLoadLyrics -> loadLyrics(action)
            LyricsAction.OnToggleSave -> toggleSave()
        }
    }

    fun loadLyrics(action: LyricsAction.OnLoadLyrics) {
        val request = LyricsRequest(
            artist = action.artist,
            track = action.track,
            album = action.album,
            image = action.image
        )

        val alreadyHandled = request == lastRequest &&
                (_uiState.value is LyricsUiState.Success || job?.isActive == true)
        if (alreadyHandled) return

        lastRequest = request
        job?.cancel()
        job = viewModelScope.launch {
            val local = withContext(Dispatchers.IO) {
                getLyricsStateUseCase(track = action.track, artist = action.artist)
            }

            if (local == null) {
                _uiState.value = LyricsUiState.Loading
            } else {
                val entity = local.lyricsEntity
                val isSaved = withContext(Dispatchers.IO) {
                    getLyricsSavedStateUseCase(
                        source = entity.source,
                        remoteId = entity.remoteId,
                        isrc = entity.isrc
                    ) != null
                }

                _uiState.value = LyricsUiState.Success(
                    isSaved = isSaved,
                    item = entity.toLyricsItem()
                )
            }

            getLyricsUseCase.invoke(
                track = action.track,
                album = action.album,
                artist = action.artist,
                image = action.image
            )
                .catch { e ->
                    if (local == null) {
                        _uiState.value = LyricsUiState.Error(
                            e.localizedMessage ?: "An unexpected error occurred"
                        )
                    }
                }
                .collect { result ->
                    val sourceStr = result.source?.name?.lowercase().orEmpty()

                    val isSaved = withContext(Dispatchers.IO) {
                        getLyricsSavedStateUseCase(
                            source = sourceStr,
                            remoteId = result.id,
                            isrc = result.isrc
                        ) != null
                    }

                    _uiState.value = LyricsUiState.Success(
                        item = result,
                        isSaved = isSaved
                    )

                    cacheLyrics(result)
                }
        }
    }

    private fun cacheLyrics(item : LyricsItem) {
        viewModelScope.launch {
            try {
                cacheLyricsUseCase(item.toEntity())
            } catch (exception : CancellationException) {
                throw exception
            } catch (_ : Exception) {

            }
        }
    }

    fun toggleSave() {
        val currentState = _uiState.value as? LyricsUiState.Success ?: return
        val wasSaved = currentState.isSaved

        viewModelScope.launch {
            _uiState.update { state ->
                if (state is LyricsUiState.Success) state.copy(isSaved = !wasSaved) else state
            }

            runCatching {
                toggleLyricsSavedUseCase(
                    isCurrentlySaved = wasSaved,
                    lyrics = currentState.item.toEntity()
                )
            }.onFailure { throwable ->
                _uiState.update { state ->
                    if (state is LyricsUiState.Success) state.copy(isSaved = wasSaved) else state
                }
                _effects.send(
                    LyricsEffect.ShowError(
                        throwable.localizedMessage ?: "Could not update saved lyrics"
                    )
                )
            }
        }
    }
}