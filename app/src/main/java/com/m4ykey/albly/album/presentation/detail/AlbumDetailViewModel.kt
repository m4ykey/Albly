@file:OptIn(ExperimentalCoroutinesApi::class)

package com.m4ykey.albly.album.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.albly.album.data.local.model.AlbumEntity
import com.m4ykey.albly.album.domain.use_case.AlbumUseCase
import com.m4ykey.albly.album.domain.use_case.GetAlbumStateUseCase
import com.m4ykey.albly.album.domain.use_case.ToggleAlbumSavedUseCase
import com.m4ykey.albly.album.domain.use_case.ToggleListenLaterSavedUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlbumDetailViewModel(
    private val useCase: AlbumUseCase,
    private val toggleAlbumSavedUseCase: ToggleAlbumSavedUseCase,
    private val toggleListenLaterSavedUseCase: ToggleListenLaterSavedUseCase,
    private val getAlbumStateUseCase: GetAlbumStateUseCase
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
                    _detail.value = DetailUiState(error = e.localizedMessage)
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

    fun toggleSave(album : AlbumEntity) {
        viewModelScope.launch {
            val currentState = _detail.value.isSaved
            toggleAlbumSavedUseCase.invoke(album, currentState)

            _detail.value = _detail.value.copy(isSaved = !currentState)
        }
    }

    fun toggleListenLater(album : AlbumEntity) {
        viewModelScope.launch {
            val currentState = _detail.value.isListenLaterSaved
            toggleListenLaterSavedUseCase.invoke(album, currentState)

            _detail.value = _detail.value.copy(isListenLaterSaved = !currentState)
        }
    }

}