package com.m4ykey.album.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.album.data.mapper.toAlbumEntity
import com.m4ykey.album.data.mapper.toAlbumRoot
import com.m4ykey.album.domain.model.detail.AlbumRoot
import com.m4ykey.album.domain.usecase.AlbumUseCase
import com.m4ykey.album.domain.usecase.CacheAlbumUseCase
import com.m4ykey.album.domain.usecase.GetAlbumStateUseCase
import com.m4ykey.album.domain.usecase.ToggleAlbumSavedUseCase
import com.m4ykey.album.domain.usecase.ToggleListenLaterSavedUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AlbumDetailViewModel(
    private val useCase: AlbumUseCase,
    private val toggleAlbumSavedUseCase: ToggleAlbumSavedUseCase,
    private val toggleListenLaterSavedUseCase: ToggleListenLaterSavedUseCase,
    private val getAlbumStateUseCase: GetAlbumStateUseCase,
    private val cacheAlbumUseCase: CacheAlbumUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState : StateFlow<DetailUiState> = _uiState.asStateFlow()

    private val _effects = Channel<AlbumDetailEffect>(Channel.BUFFERED)
    val effects : Flow<AlbumDetailEffect> = _effects.receiveAsFlow()

    fun onAction(action: AlbumDetailAction) {
        when (action) {
            AlbumDetailAction.OnToggleSave -> toggleSave()
            AlbumDetailAction.OnToggleListenLater -> toggleListenLater()
            is AlbumDetailAction.OnLoadAlbum -> loadAlbum(action.id)
            AlbumDetailAction.OnRefresh -> refresh()
        }
    }

    private var loadedId : Int? = null
    private var loadJob : Job? = null

    private fun refresh() {
        val id = loadedId ?: return

        when (val current = _uiState.value) {
            is DetailUiState.Success -> if (current.isRefreshing) return
            is DetailUiState.Error -> if (current.isRefreshing) return
            DetailUiState.Loading -> return
        }

        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.update { state ->
                when (state) {
                    is DetailUiState.Success -> state.copy(isRefreshing = true)
                    is DetailUiState.Error -> state.copy(isRefreshing = true)
                    else -> state
                }
            }

            fetchRemoteAlbum(id).fold(
                onSuccess = { album ->
                    _uiState.update { state ->
                        when (state) {
                            is DetailUiState.Success -> state.copy(item = album, isRefreshing = false)
                            else -> DetailUiState.Success(item = album)
                        }
                    }
                    cacheAlbum(album)
                },
                onFailure = { error ->
                    val message = error.localizedMessage ?: "Unknown error"
                    _uiState.update { state ->
                        when (state) {
                            is DetailUiState.Success -> state.copy(isRefreshing = false)
                            is DetailUiState.Error -> state.copy(isRefreshing = false, message = message)
                            else -> state
                        }
                    }
                    if (_uiState.value is DetailUiState.Success) {
                        _effects.send(AlbumDetailEffect.ShowError(message))
                    }
                }
            )
        }
    }

    private fun loadAlbum(id : Int) {
        if (loadedId == id && _uiState.value is DetailUiState.Success) return

        loadedId = id
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            val remoteAlbum = async { fetchRemoteAlbum(id) }

            val local = try {
                getAlbumStateUseCase(id)
            } catch (exception : CancellationException) {
                throw exception
            } catch (_ : Exception) {
                null
            }

            if (local == null) {
                _uiState.value = DetailUiState.Loading
            } else {
                _uiState.value = DetailUiState.Success(
                    isSaved = local.isAlbumSaved != null,
                    isListenLaterSaved = local.isListenLaterSaved != null,
                    item = local.albumEntity.toAlbumRoot()
                )
            }

            remoteAlbum.await().fold(
                onSuccess = { album ->
                    _uiState.update { state ->
                        when (state) {
                            is DetailUiState.Success -> state.copy(item = album, isRefreshing = false)
                            else -> DetailUiState.Success(
                                item = album,
                                isSaved = local?.isAlbumSaved != null,
                                isListenLaterSaved = local?.isListenLaterSaved != null
                            )
                        }
                    }
                    cacheAlbum(album)
                },
                onFailure = { error ->
                    if (local == null) {
                        _uiState.value = DetailUiState.Error(
                            message = error.localizedMessage ?: "Unknown error"
                        )
                    }
                }
            )
        }
    }

    private fun cacheAlbum(album : AlbumRoot) {
        viewModelScope.launch {
            try {
                cacheAlbumUseCase(album.toAlbumEntity())
            } catch (exception : CancellationException) {
                throw exception
            } catch (_ : Exception) {

            }
        }
    }

    private suspend fun fetchRemoteAlbum(id : Int) : Result<AlbumRoot> {
        return try {
            Result.success(useCase.invoke(id).first())
        } catch (exception : CancellationException) {
            throw exception
        } catch (exception : Exception) {
            Result.failure(exception)
        }
    }

    private fun toggleSave() {
        val state = _uiState.value as? DetailUiState.Success ?: return

        viewModelScope.launch {
            try {
                toggleAlbumSavedUseCase(
                    isCurrentlySaved = state.isSaved,
                    album = state.item.toAlbumEntity()
                )
                _uiState.update { current ->
                    if (current is DetailUiState.Success) {
                        current.copy(isSaved = !current.isSaved)
                    } else {
                        current
                    }
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception : Exception) {
                _effects.send(
                    AlbumDetailEffect.ShowError(
                        exception.message ?: "Failed to update saved state"
                    )
                )
            }
        }
    }

    fun toggleListenLater() {
        val state = _uiState.value as? DetailUiState.Success ?: return

        viewModelScope.launch {
            try {
                toggleListenLaterSavedUseCase(
                    isCurrentlySaved = state.isListenLaterSaved,
                    album = state.item.toAlbumEntity()
                )
                _uiState.update { current ->
                    if (current is DetailUiState.Success) {
                        current.copy(isListenLaterSaved = !current.isListenLaterSaved)
                    } else {
                        current
                    }
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception : Exception) {
                _effects.send(
                    AlbumDetailEffect.ShowError(
                        exception.message ?: "Failed to update listen later state"
                    )
                )
            }
        }
    }
}