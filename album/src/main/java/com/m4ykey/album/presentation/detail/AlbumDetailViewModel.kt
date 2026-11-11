package com.m4ykey.album.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.album.data.mapper.toAlbumEntity
import com.m4ykey.album.data.mapper.toAlbumRoot
import com.m4ykey.album.domain.usecase.AlbumUseCase
import com.m4ykey.album.domain.usecase.GetAlbumStateUseCase
import com.m4ykey.album.domain.usecase.GetLocalAlbumUseCase
import com.m4ykey.album.domain.usecase.ToggleAlbumSavedUseCase
import com.m4ykey.album.domain.usecase.ToggleListenLaterSavedUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlbumDetailViewModel(
    private val useCase: AlbumUseCase,
    private val toggleAlbumSavedUseCase: ToggleAlbumSavedUseCase,
    private val toggleListenLaterSavedUseCase: ToggleListenLaterSavedUseCase,
    private val getAlbumStateUseCase: GetAlbumStateUseCase,
    private val getLocalAlbumUseCase : GetLocalAlbumUseCase
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
        }
    }

    private fun loadAlbum(id : Int) {
        if (_uiState.value is DetailUiState.Success) return

        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading

            val localState = withContext(Dispatchers.IO) {
                getAlbumStateUseCase.invoke(id)
            }

            useCase.invoke(id)
                .catch { e ->
                    if (e is CancellationException) throw e

                    val localAlbum = withContext(Dispatchers.IO) {
                        getLocalAlbumUseCase(id)
                    }
                    val domainAlbum = localAlbum?.toAlbumRoot()

                    if (domainAlbum != null) {
                        _uiState.value = DetailUiState.Success(
                            item = domainAlbum,
                            isSaved = localState?.isAlbumSaved != null,
                            isListenLaterSaved = localState?.isListenLaterSaved != null
                        )
                    } else {
                        _uiState.value = DetailUiState.Error(
                            message = e.localizedMessage ?: "Unknown Error")
                    }
                }
                .collect { result ->
                    _uiState.value = DetailUiState.Success(
                        item = result,
                        isSaved = localState?.isAlbumSaved != null,
                        isListenLaterSaved = localState?.isListenLaterSaved != null
                    )
                }
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