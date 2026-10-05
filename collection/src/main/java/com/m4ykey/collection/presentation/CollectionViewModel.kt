@file:OptIn(ExperimentalCoroutinesApi::class)

package com.m4ykey.collection.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.album.data.local.model.AlbumSort
import com.m4ykey.album.domain.usecase.GetSavedAlbumsUseCase
import com.m4ykey.artist.domain.GetSavedArtistUseCase
import com.m4ykey.collection.presentation.type.ListType
import com.m4ykey.collection.presentation.type.ListTypeState
import com.m4ykey.collection.presentation.type.ListViewType
import com.m4ykey.core.ui.hide
import com.m4ykey.core.ui.show
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CollectionViewModel(
    private val getSavedAlbumsUseCase: GetSavedAlbumsUseCase,
    private val getSavedArtistUseCase: GetSavedArtistUseCase
) : ViewModel() {

    private val _isLinkDialogVisible = MutableStateFlow(false)
    val isLinkDialogVisible = _isLinkDialogVisible.asStateFlow()

    private val _isSearchVisible = MutableStateFlow(false)
    val isSearchVisible = _isSearchVisible.asStateFlow()

    private val _isSortDialogVisible = MutableStateFlow(false)
    val isSortDialogVisible = _isSortDialogVisible.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _collectionUiEvent = MutableSharedFlow<CollectionUiEvent>()
    val collectionUiEvent = _collectionUiEvent.asSharedFlow()

    private val _listTypeState = MutableStateFlow(
        ListTypeState(
            view = ListViewType.GRID,
            type = ListType.ALBUM,
            sort = AlbumSort.LATEST
        )
    )

    val listTypeState = _listTypeState.asStateFlow()

    val uiState : StateFlow<CollectionUiState> =
        combine(
            _searchQuery,
            _listTypeState
        ) { query, listTypeState ->
            query to listTypeState
        }
        .flatMapLatest { (query, listTypeState) ->
            when (listTypeState.type) {
                ListType.ALBUM -> {
                    getSavedAlbumsUseCase(query, listTypeState.sort)
                        .map { albums ->
                            CollectionUiState(
                                albums = albums,
                                isLoading = false
                            )
                        }
                }
                ListType.ARTIST -> {
                    getSavedArtistUseCase(query)
                        .map { artists ->
                            CollectionUiState(
                                isLoading = false,
                                artists = artists
                            )
                        }
                }
            }
                .onStart {
                    emit(CollectionUiState(isLoading = true))
                }
                .catch { exception ->
                    emit(CollectionUiState(isLoading = false, error = exception.message))
                }
        }
        .stateIn(
            initialValue = CollectionUiState(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    fun hideSortDialog() = hide(_isSortDialogVisible)
    fun showSortDialog() = show(_isSortDialogVisible)

    fun showSearch() = show(_isSearchVisible)
    fun hideSearch() = hide(_isSearchVisible)

    fun hideLinkDialog() = hide(_isLinkDialogVisible)
    fun showLinkDialog() = show(_isLinkDialogVisible)

    fun clearTextField() {
        _searchQuery.value = ""
    }

    fun onAction(action : CollectionTypeAction) {
        when (action) {
            is CollectionTypeAction.OnLinkClick -> {
                viewModelScope.launch {
                    _collectionUiEvent.emit(CollectionUiEvent.OnLinkClick(action.link))
                }
            }
            is CollectionTypeAction.OnQueryChange -> {
                _searchQuery.value = action.text
            }
            is CollectionTypeAction.OnChangeSort -> {
                _listTypeState.update {
                    it.copy(sort = action.sort)
                }
            }
            is CollectionTypeAction.OnChangeView -> {
                _listTypeState.update {
                    it.copy(view = action.view)
                }
            }
            is CollectionTypeAction.OnChangeList -> {
                _listTypeState.update {
                    it.copy(type = action.list)
                }
            }
        }
    }
}