package com.m4ykey.albly.album.presentation.listen_later

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.albly.album.data.local.model.AlbumEntity
import com.m4ykey.albly.album.domain.use_case.GetListenLaterAlbumsUseCase
import com.m4ykey.albly.album.domain.use_case.GetRandomAlbumUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListenLaterViewModel(
    private val getRandomAlbumUseCase: GetRandomAlbumUseCase,
    private val getListenLaterAlbumsUseCase: GetListenLaterAlbumsUseCase
) : ViewModel() {

    private val _albums = MutableStateFlow<List<AlbumEntity>>(emptyList())
    val albums = _albums.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

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