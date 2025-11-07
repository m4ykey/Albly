package com.m4ykey.albly.album.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.albly.album.domain.use_case.AlbumUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    private val useCase: AlbumUseCase
) : ViewModel() {

    private val _detail = MutableStateFlow(DetailUiState())
    val detail = _detail.asStateFlow()

    fun getAlbumById(id : String) {
        viewModelScope.launch {
            _detail.value = DetailUiState(loading = true)

            useCase.invoke(id)
                .catch { e -> _detail.value = DetailUiState(error = e.localizedMessage) }
                .collect { result -> _detail.value = DetailUiState(item = result) }
        }
    }

}