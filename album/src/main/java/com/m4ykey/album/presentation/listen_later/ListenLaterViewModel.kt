package com.m4ykey.album.presentation.listen_later

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.album.domain.usecase.GetListenLaterAlbumsUseCase
import com.m4ykey.album.domain.usecase.GetRandomAlbumUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListenLaterViewModel(
    private val getRandomAlbumUseCase: GetRandomAlbumUseCase,
    private val getListenLaterAlbumsUseCase: GetListenLaterAlbumsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListenLaterUiState())
    val uiState = _uiState.asStateFlow()

    fun getRandomAlbum() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            try {
                getRandomAlbumUseCase().collectLatest { album ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            randomAlbum = album
                        )
                    }
                }
            } catch (e : Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    fun clearAlbum() {
        _uiState.update {
            it.copy(randomAlbum = null)
        }
    }

    fun loadAlbums() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            try {
                val result = getListenLaterAlbumsUseCase()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        albums = result
                    )
                }
            } catch (e : Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }
}