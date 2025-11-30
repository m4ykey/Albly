@file:OptIn(ExperimentalCoroutinesApi::class)

package com.m4ykey.albly.album.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.m4ykey.albly.album.domain.use_case.AlbumUseCase
import com.m4ykey.albly.album.domain.use_case.TrackUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class AlbumDetailViewModel(
    private val useCase: AlbumUseCase,
    private val trackUseCase: TrackUseCase
) : ViewModel() {

    private val _detail = MutableStateFlow(DetailUiState())
    val detail = _detail.asStateFlow()

    private val _albumId = MutableStateFlow<String?>("")

    val trackResults = _albumId
        .filterNotNull()
        .flatMapLatest { id -> trackUseCase.invoke(id = id).cachedIn(viewModelScope) }

    fun setAlbum(id : String) {
        _albumId.value = id
    }

    fun getAlbumById(id : String) {
        viewModelScope.launch {
            _detail.value = DetailUiState(loading = true)
            useCase.invoke(id)
                .catch { e -> _detail.value = DetailUiState(error = e.localizedMessage) }
                .collect { result -> _detail.value = DetailUiState(item = result) }
        }
    }

}