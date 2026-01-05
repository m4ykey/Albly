package com.m4ykey.album.presentation.listen_later

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.album.data.local.model.AlbumEntity
import com.m4ykey.album.domain.use_case.GetListenLaterAlbumsUseCase
import com.m4ykey.album.domain.use_case.GetRandomAlbumUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListenLaterViewModel(
    private val getRandomAlbumUseCase: GetRandomAlbumUseCase,
    private val getListenLaterAlbumsUseCase: GetListenLaterAlbumsUseCase
) : ViewModel() {

    private val _albums = MutableStateFlow<List<AlbumEntity>>(emptyList())
    val albums = _albums.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _randomAlbum = MutableStateFlow<AlbumEntity?>(null)
    val randomAlbum = _randomAlbum.asStateFlow()

    fun getRandomAlbum() {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                getRandomAlbumUseCase().collectLatest { album ->
                    _randomAlbum.value = album
                    _isLoading.value = false
                }
            } catch (e : Exception) {

            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearAlbum() {
        _randomAlbum.value = null
    }

    fun loadAlbums() {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val result = getListenLaterAlbumsUseCase()
                _albums.value = result
            } catch (e : Exception) {

            } finally {
                _isLoading.value = false
            }
        }
    }
}