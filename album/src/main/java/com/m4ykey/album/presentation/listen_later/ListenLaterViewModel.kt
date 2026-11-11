package com.m4ykey.album.presentation.listen_later

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.album.domain.usecase.GetListenLaterAlbumsUseCase
import com.m4ykey.album.domain.usecase.GetRandomAlbumUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListenLaterViewModel(
    private val getRandomAlbumUseCase: GetRandomAlbumUseCase,
    private val getListenLaterAlbumsUseCase: GetListenLaterAlbumsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListenLaterUiState())
    val uiState : StateFlow<ListenLaterUiState> = _uiState.asStateFlow()

    private val _effects = Channel<ListenLaterEffect>(Channel.BUFFERED)
    val effects : Flow<ListenLaterEffect> = _effects.receiveAsFlow()

    init {
        loadAlbums()
    }

    fun onAction(action: ListenLaterAction) {
        when (action) {
            ListenLaterAction.OnLoadAlbums -> loadAlbums()
            ListenLaterAction.OnRandomAlbumClick -> openRandomAlbum()
        }
    }

    private fun loadAlbums() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val albums = getListenLaterAlbumsUseCase()
                _uiState.update { it.copy(isLoading = false, albums = albums) }
            } catch (exception : CancellationException) {
                throw exception
            } catch (exception : Exception) {
                _uiState.update { it.copy(isLoading = false) }
                _effects.send(
                    ListenLaterEffect.ShowError(
                        exception.message ?: "Failed to load albums"
                    )
                )
            }
        }
    }

    private fun sendEffect(effect: ListenLaterEffect) {
        viewModelScope.launch { _effects.send(effect) }
    }

    private fun openRandomAlbum() {
        val albums = _uiState.value.albums

        when {
            albums.isEmpty() -> sendEffect(ListenLaterEffect.AlbumListEmpty)
            albums.size < 2 -> sendEffect(ListenLaterEffect.NotEnoughAlbums)
            else -> viewModelScope.launch {
                try {
                    val album = getRandomAlbumUseCase().first()
                    _effects.send(ListenLaterEffect.OpenAlbum(album.id))
                } catch (exception : CancellationException) {
                    throw exception
                } catch (exception : Exception) {
                    _effects.send(
                        ListenLaterEffect.ShowError(
                            exception.message ?: "Failed to get random album"
                        )
                    )
                }
            }
        }
    }
}