package com.m4ykey.albly.album.presentation.new_release

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.m4ykey.albly.album.domain.model.AlbumItem
import com.m4ykey.albly.album.domain.use_case.NewReleaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class NewReleaseViewModel(
    private val useCase: NewReleaseUseCase
) : ViewModel() {

    val newRelease : Flow<PagingData<AlbumItem>> =
        useCase.invoke()
            .cachedIn(viewModelScope)

    private val _uiEvent = MutableSharedFlow<NewReleaseUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onAction(action : NewReleaseAction) {
        viewModelScope.launch {
            when (action) {
                is NewReleaseAction.OnAlbumClick -> {
                    _uiEvent.emit(NewReleaseUiEvent.OnAlbumClick(action.id))
                }
            }
        }
    }
}