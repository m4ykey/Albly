@file:OptIn(ExperimentalCoroutinesApi::class)

package com.m4ykey.collection.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.album.data.local.model.AlbumEntity
import com.m4ykey.album.domain.use_case.GetSavedAlbumsUseCase
import com.m4ykey.core.model.type.AlbumType
import com.m4ykey.collection.presentation.type.album.AlbumTypeState
import com.m4ykey.core.ext.hide
import com.m4ykey.core.ext.show
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CollectionViewModel(
    private val getSavedAlbumsUseCase: GetSavedAlbumsUseCase
) : ViewModel() {

    private val _isLinkDialogVisible = MutableStateFlow(false)
    val isLinkDialogVisible = _isLinkDialogVisible.asStateFlow()

    private val _isSearchVisible = MutableStateFlow(false)
    val isSearchVisible = _isSearchVisible.asStateFlow()

    private val _albumType = MutableStateFlow(AlbumTypeState())
    val albumType = _albumType.asStateFlow()

    private val _isSortDialogVisible = MutableStateFlow(false)
    val isSortDialogVisible = _isSortDialogVisible.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _collectionUiEvent = MutableSharedFlow<CollectionUiEvent>()
    val collectionUiEvent = _collectionUiEvent.asSharedFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    val albums : StateFlow<List<AlbumEntity>> = combine(
        _searchQuery,
        _albumType
    ) { query, typeState ->
        query to typeState.type
    }.flatMapLatest { (query, type) ->
        getSavedAlbumsUseCase(query, type = null).map { list ->
            if (type == null) list
            else list.filter { it.albumType.equals(type.name, ignoreCase = true) }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
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

    fun updateType(type : AlbumType?) {
        _albumType.update { it.copy(type = type) }
    }

    fun onAction(action : CollectionTypeAction) {
        when (action) {
            is CollectionTypeAction.OnTypeClick -> {
                updateType(action.type)
            }
            is CollectionTypeAction.OnLinkClick -> {
                viewModelScope.launch {
                    _collectionUiEvent.emit(CollectionUiEvent.OnLinkClick(action.link))
                }
            }
            is CollectionTypeAction.OnQueryChange -> {
                _searchQuery.value = action.text
            }
        }
    }
}