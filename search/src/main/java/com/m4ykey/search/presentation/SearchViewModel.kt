@file:OptIn(ExperimentalCoroutinesApi::class)

package com.m4ykey.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.m4ykey.core.paging.distinctById
import com.m4ykey.search.domain.usecase.SearchAlbumUseCase
import com.m4ykey.search.domain.usecase.SearchArtistUseCase
import com.m4ykey.search.domain.usecase.SearchLyricsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val albumUseCase: SearchAlbumUseCase,
    private val artistUseCase : SearchArtistUseCase,
    private val lyricsUseCase: SearchLyricsUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchType = MutableStateFlow(SearchTypeState())
    val searchType = _searchType.map { it.type }
        .stateIn(
            scope = viewModelScope,
            initialValue = SearchType.ALBUM,
            started = SharingStarted.WhileSubscribed(5000)
        )

    private val _searchUiEvent = MutableSharedFlow<SearchUiEvent>()
    val searchUiEvent = _searchUiEvent.asSharedFlow()

    private val _activeSearchQuery = MutableStateFlow("")
    val activeSearchQuery = _activeSearchQuery.asStateFlow()

    fun updateType(type: SearchType) {
        _searchType.update { it.copy(type = type) }
    }

    val searchLyricsResults = searchFlow(
        SearchType.LYRICS,
        source = { lyricsUseCase(query = it) }
    ) { it.isrc }

    val searchArtistResults = searchFlow(
        SearchType.ARTIST,
        source = { artistUseCase(query = it, type = "artist") }
    ) { it.id }

    val searchAlbumResults = searchFlow(
        SearchType.ALBUM,
        source = { albumUseCase(query = it, type = "album") }
    ) { it.id ?: 0 }

    fun onAction(action: SearchTypeAction) {
        viewModelScope.launch {
            when (action) {
                is SearchTypeAction.OnTrackClick -> {
                    _searchUiEvent.emit(SearchUiEvent.OnTrackClick(action.title, action.artist, action.img, action.album))
                }
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

                is SearchTypeAction.OnArtistClick -> {
                    _searchUiEvent.emit(SearchUiEvent.OnArtistClick(action.id))
                }
            }
        }
    }

    private fun <T: Any> searchFlow(
        targetType : SearchType,
        source : (query : String) -> Flow<PagingData<T>>,
        idSelector : (T) -> Any
    ) : Flow<PagingData<T>> =
        combine(_activeSearchQuery, _searchType) { query, state -> query to state.type }
            .flatMapLatest { (query, type) ->
                if (query.isBlank() || type != targetType) {
                    flowOf(PagingData.empty())
                } else {
                    source(query).map { it.distinctById(idSelector) }
                }
            }
            .cachedIn(viewModelScope)
}