@file:OptIn(ExperimentalCoroutinesApi::class)

package com.m4ykey.album.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.album.data.mapper.toDomain
import com.m4ykey.album.domain.use_case.AlbumUseCase
import com.m4ykey.album.domain.use_case.GetAlbumStateUseCase
import com.m4ykey.album.domain.use_case.GetLocalAlbumUseCase
import com.m4ykey.album.domain.use_case.ToggleAlbumSavedUseCase
import com.m4ykey.album.domain.use_case.ToggleListenLaterSavedUseCase
import com.m4ykey.track.data.local.model.TrackEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.m4ykey.album.data.local.model.AlbumEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlbumDetailViewModel(
    private val useCase: AlbumUseCase,
    private val toggleAlbumSavedUseCase: ToggleAlbumSavedUseCase,
    private val toggleListenLaterSavedUseCase: ToggleListenLaterSavedUseCase,
    private val getAlbumStateUseCase: GetAlbumStateUseCase,
    private val getLocalAlbumUseCase : GetLocalAlbumUseCase
) : ViewModel() {

    private val _detail = MutableStateFlow(DetailUiState())
    val detail = _detail.asStateFlow()

    fun getAlbumById(id : String) {
        viewModelScope.launch {
            _detail.value = DetailUiState(loading = true)

            val localState = withContext(Dispatchers.IO) {
                getAlbumStateUseCase.invoke(id)
            }

            useCase.invoke(id)
                .catch { e ->
                    val localAlbum = withContext(Dispatchers.IO) {
                        getLocalAlbumUseCase(id)
                    }

                    if (localAlbum != null) {
                        _detail.value = DetailUiState(
                            item = localAlbum.toDomain(),
                            loading = false,
                            isSaved = localState?.isAlbumSaved != null,
                            isListenLaterSaved = localState?.isListenLaterSaved != null
                        )
                    } else {
                        _detail.value = DetailUiState(error = e.localizedMessage)
                    }
                }
                .collect { result ->
                    _detail.value = DetailUiState(
                        item = result,
                        loading = false,
                        isSaved = localState?.isAlbumSaved != null,
                        isListenLaterSaved = localState?.isListenLaterSaved != null
                    )
                }
        }
    }

    fun toggleSave(album : AlbumEntity, tracks : List<TrackEntity>) {
        viewModelScope.launch {
            val currentState = _detail.value.isSaved
            toggleAlbumSavedUseCase(
                album = album,
                isCurrentlySaved = currentState,
                tracks = tracks
            )

            _detail.value = _detail.value.copy(isSaved = !currentState)
        }
    }

    fun toggleListenLater(album : AlbumEntity, tracks : List<TrackEntity>) {
        viewModelScope.launch {
            val currentState = _detail.value.isListenLaterSaved
            toggleListenLaterSavedUseCase.invoke(
                album = album,
                isCurrentlySaved = currentState,
                tracks = tracks
            )

            _detail.value = _detail.value.copy(isListenLaterSaved = !currentState)
        }
    }
}