package com.m4ykey.albly.collection.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.albly.collection.presentation.type.album.AlbumType
import com.m4ykey.albly.collection.presentation.CollectionTypeAction
import com.m4ykey.albly.collection.presentation.type.album.AlbumTypeState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CollectionViewModel : ViewModel() {

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
        viewModelScope.launch {
            when (action) {
                is CollectionTypeAction.OnTypeClick -> {
                    _collectionUiEvent.emit(CollectionUiEvent.ChangeType(action.type))
                }
                is CollectionTypeAction.OnLinkClick -> {
                    _collectionUiEvent.emit(CollectionUiEvent.OnLinkClick(action.link))
                }
                is CollectionTypeAction.OnQueryChange -> {
                    _searchQuery.value = action.text
                }
            }
        }
    }

    private fun show(state : MutableStateFlow<Boolean>) {
        state.value = true
    }
    private fun hide(state : MutableStateFlow<Boolean>) {
        state.value = false
    }

}