package com.m4ykey.album.presentation.new_release

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.m4ykey.album.domain.model.new_release.NewReleaseResult
import com.m4ykey.album.domain.usecase.NewReleaseUseCase
import com.m4ykey.album.presentation.new_release.NewReleaseEffect.*
import com.m4ykey.core.paging.distinctById
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewReleaseViewModel(
    useCase: NewReleaseUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewReleaseUiState())
    val uiState : StateFlow<NewReleaseUiState> = _uiState.asStateFlow()

    val newRelease : Flow<PagingData<NewReleaseResult>> =
        useCase.invoke()
            .map { pagingData -> pagingData.distinctById { it.masterId } }
            .cachedIn(viewModelScope)

    private val _effects = Channel<NewReleaseEffect>(Channel.BUFFERED)
    val uiEvent : Flow<NewReleaseEffect> = _effects.receiveAsFlow()

    fun onAction(action : NewReleaseAction) {
        viewModelScope.launch {
            when (action) {
                is NewReleaseAction.OnAlbumClick -> { _effects.send(OnAlbumClick(action.id)) }
                NewReleaseAction.OnHideAlbumDialog -> reduce { copy(selectedAlbumId = null) }
                is NewReleaseAction.OnLongAlbumClick -> { reduce { copy(selectedAlbumId = action.id) } }
            }
        }
    }

    private inline fun reduce(transform : NewReleaseUiState.() -> NewReleaseUiState) {
        _uiState.update { it.transform() }
    }
}