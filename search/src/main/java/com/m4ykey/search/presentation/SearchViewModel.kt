@file:OptIn(ExperimentalCoroutinesApi::class)

package com.m4ykey.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.m4ykey.search.domain.use_case.SearchAlbumUseCase
import com.m4ykey.search.domain.use_case.SearchArtistUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val albumUseCase: SearchAlbumUseCase,
    private val artistUseCase : SearchArtistUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchType = MutableStateFlow(SearchTypeState())
    val searchType = _searchType.asStateFlow()

    private val _searchUiEvent = MutableSharedFlow<SearchUiEvent>()
    val searchUiEvent = _searchUiEvent.asSharedFlow()

    private val _activeSearchQuery = MutableStateFlow("")
    val activeSearchQuery = _activeSearchQuery.asStateFlow()

    fun updateType(type: SearchType) {
        _searchType.update { it.copy(type = type) }
    }

    val searchArtistResults = combine(
        _activeSearchQuery,
        _searchType
    ) { query, typeState -> query to typeState.type }
        .flatMapLatest { (query, type) ->
            if (query.isBlank()) {
                emptyFlow()
            } else {
                artistUseCase(q = query, type = type.name.lowercase())
            }
    }
        .cachedIn(viewModelScope)
        .stateIn(
            scope = viewModelScope,
            initialValue = PagingData.empty(),
            started = SharingStarted.Lazily
        )

    val searchAlbumResults = combine(
        _activeSearchQuery,
        _searchType
    ) { query, typeState -> query to typeState.type }
        .flatMapLatest { (query, type) ->
            if (query.isBlank()) {
                emptyFlow()
            } else {
                albumUseCase(q = query, type = type.name.lowercase())
            }
    }
        .cachedIn(viewModelScope)
        .stateIn(
            scope = viewModelScope,
            initialValue = PagingData.empty(),
            started = SharingStarted.Lazily
        )


    fun onAction(action: SearchTypeAction) {
        viewModelScope.launch {
            when (action) {
                is SearchTypeAction.OnTypeClick -> {
                    updateType(action.type)
                    _searchUiEvent.emit(SearchUiEvent.ChangeType(action.type))
                }

                is SearchTypeAction.OnQueryChange -> {
                    _searchQuery.value = action.query
                }

                is SearchTypeAction.OnSearchClick -> {
                    _activeSearchQuery.value = _searchQuery.value
                }

                is SearchTypeAction.OnAlbumClick -> {
                    _searchUiEvent.emit(SearchUiEvent.OnAlbumClick(action.id))
                }
            }
        }
    }
}