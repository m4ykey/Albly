package com.m4ykey.albly.collection.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.albly.collection.presentation.type.album.AlbumType
import com.m4ykey.albly.collection.presentation.type.album.AlbumTypeAction
import com.m4ykey.albly.collection.presentation.type.album.AlbumTypeState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CollectionViewModel : ViewModel() {

    private val _albumType = MutableStateFlow(AlbumTypeState())
    val albumType = _albumType.asStateFlow()

    private val _collectionUiEvent = MutableSharedFlow<CollectionUiEvent>()
    val collectionUiEvent = _collectionUiEvent.asSharedFlow()

    fun updateType(type : AlbumType?) {
        _albumType.update { it.copy(type = type) }
    }

    fun onAction(action : AlbumTypeAction) {
        viewModelScope.launch {
            when (action) {
                is AlbumTypeAction.OnTypeClick -> {
                    _collectionUiEvent.emit(CollectionUiEvent.ChangeType(action.type))
                }
                is AlbumTypeAction.OnLinkClick -> {
                    _collectionUiEvent.emit(CollectionUiEvent.OnLinkClick(action.link))
                }
            }
        }
    }

}