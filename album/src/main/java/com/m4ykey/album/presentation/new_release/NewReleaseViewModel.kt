package com.m4ykey.album.presentation.new_release

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.m4ykey.album.domain.model.new_release.NewReleaseResult
import com.m4ykey.album.domain.usecase.NewReleaseUseCase
import com.m4ykey.core.paging.distinctById
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class NewReleaseViewModel(
    useCase: NewReleaseUseCase
) : ViewModel() {

    val newRelease : Flow<PagingData<NewReleaseResult>> =
        useCase.invoke()
            .map { pagingData -> pagingData.distinctById { it.master_id } }
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