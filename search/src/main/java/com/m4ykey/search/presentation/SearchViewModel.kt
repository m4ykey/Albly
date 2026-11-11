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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val albumUseCase: SearchAlbumUseCase,
    private val artistUseCase : SearchArtistUseCase,
    private val lyricsUseCase: SearchLyricsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState : StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _effects = Channel<SearchEffect>(Channel.BUFFERED)
    val effects : Flow<SearchEffect> = _effects.receiveAsFlow()

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
                is SearchTypeAction.OnQueryChange -> reduce { copy(query = action.query) }
                is SearchTypeAction.OnAlbumClick -> sendEffects(SearchEffect.OnAlbumClick(action.id))
                is SearchTypeAction.OnArtistClick -> sendEffects(SearchEffect.OnArtistClick(action.id))
                SearchTypeAction.OnSearchClick -> reduce { copy(activeQuery = query) }
                is SearchTypeAction.OnTrackClick -> sendEffects(
                    SearchEffect.OnTrackClick(
                        title = action.title,
                        img = action.img,
                        artist = action.artist,
                        album = action.album
                    )
                )
                is SearchTypeAction.OnTypeClick -> reduce { copy(type = action.type) }
            }
        }
    }

    private inline fun reduce(transform : SearchUiState.() -> SearchUiState) {
        _uiState.update { it.transform() }
    }

    private fun sendEffects(effect: SearchEffect) {
        viewModelScope.launch { _effects.send(effect) }
    }

    private fun <T: Any> searchFlow(
        targetType : SearchType,
        source : (query : String) -> Flow<PagingData<T>>,
        idSelector : (T) -> Any
    ) : Flow<PagingData<T>> =
        _uiState
            .map { state -> SearchQuery(state.activeQuery, state.type) }
            .distinctUntilChanged()
            .flatMapLatest { search -> search.toPagingFlow(targetType, source, idSelector) }
            .cachedIn(viewModelScope)

    private fun <T: Any> SearchQuery.toPagingFlow(
        targetType: SearchType,
        source : (query : String) -> Flow<PagingData<T>>,
        idSelector : (T) -> Any
    ) : Flow<PagingData<T>> =
        if (query.isBlank() || type != targetType) {
            flowOf(PagingData.empty())
        } else {
            source(query).map { it.distinctById(idSelector) }
        }
}