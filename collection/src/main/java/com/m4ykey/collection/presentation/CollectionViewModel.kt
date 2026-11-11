@file:OptIn(ExperimentalCoroutinesApi::class)

package com.m4ykey.collection.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.album.domain.usecase.GetSavedAlbumsUseCase
import com.m4ykey.artist.domain.use_case.GetSavedArtistUseCase
import com.m4ykey.collection.presentation.type.ListType
import com.m4ykey.collection.presentation.type.ListTypeState
import com.m4ykey.collection.presentation.type.ListViewType
import com.m4ykey.collection.repository.CollectionRepository
import com.m4ykey.core.Sort
import com.m4ykey.lyrics.domain.usecase.GetLyricsSavedUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CollectionViewModel(
    private val getSavedAlbumsUseCase: GetSavedAlbumsUseCase,
    private val getSavedArtistUseCase: GetSavedArtistUseCase,
    private val getSavedLyricsUseCase : GetLyricsSavedUseCase,
    private val repository: CollectionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CollectionUiState(isLoading = true))
    val uiState : StateFlow<CollectionUiState> = _uiState.asStateFlow()

    private val _effects = Channel<CollectionEffect>(Channel.BUFFERED)
    val effects : Flow<CollectionEffect> = _effects.receiveAsFlow()

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(listTypeState = readPreferences()) }
            observeContent()
            observePreferences()
        }
    }

    fun onAction(action : CollectionAction) {
        when (action) {
            is CollectionAction.OnQueryChange -> reduce { copy(searchQuery = action.query) }
            is CollectionAction.OnChangeList -> {
                reduce { copy(listTypeState = listTypeState.copy(list = action.list)) }
                persist { repository.saveListOption(action.list) }
            }
            is CollectionAction.OnChangeView -> {
                reduce { copy(listTypeState = listTypeState.copy(view = action.view)) }
                persist { repository.saveViewOption(action.view) }
            }
            is CollectionAction.OnChangeSort -> {
                reduce { copy(listTypeState = listTypeState.copy(sort = action.sort)) }
                persist { repository.saveSortOption(action.sort) }
            }
            is CollectionAction.OnLinkClick -> handleLink(action.url)
            CollectionAction.OnClearQuery -> reduce { copy(searchQuery = "") }
            CollectionAction.OnShowSearch -> reduce { copy(isSearchVisible = true) }
            CollectionAction.OnHideSearch -> reduce { copy(isSearchVisible = false) }
            CollectionAction.OnShowLinkDialog -> reduce { copy(isLinkDialogVisible = true) }
            CollectionAction.OnHideLinkDialog -> reduce { copy(isLinkDialogVisible = false) }
            CollectionAction.OnHideSortDialog -> reduce { copy(isSortDialogVisible = false) }
            CollectionAction.OnShowSortDialog -> reduce { copy(isSortDialogVisible = true) }
        }
    }

    private fun handleLink(url : String) {

    }

    private fun sendEffect(effect: CollectionEffect) {
        viewModelScope.launch { _effects.send(effect) }
    }

    private fun observeContent() {
        viewModelScope.launch {
            _uiState
                .map { state ->
                    ContentKey(
                        query = state.searchQuery,
                        type = state.listTypeState.list,
                        sort = if (state.listTypeState.list == ListType.ARTIST) {
                            null
                        } else {
                            state.listTypeState.sort
                        }
                    )
                }
                .distinctUntilChanged()
                .flatMapLatest { key -> contentUpdates(key) }
                .collect { update -> applyContentUpdate(update) }
        }
    }

    private fun contentUpdates(key : ContentKey) : Flow<ContentUpdate> {
        val source : Flow<ContentUpdate> = when (key.type) {
            ListType.ALBUM -> getSavedAlbumsUseCase(key.query, key.sort ?: Sort.LATEST)
                .map { albums -> ContentUpdate(isLoading = false, albums = albums) }
            ListType.ARTIST -> getSavedArtistUseCase(key.query)
                .map { artists -> ContentUpdate(isLoading = false, artists = artists) }
            ListType.LYRICS -> getSavedLyricsUseCase(key.query, key.sort ?: Sort.LATEST)
                .map { lyrics -> ContentUpdate(isLoading = false, lyrics = lyrics) }
        }

        return source
            .onStart { emit(ContentUpdate(isLoading = true)) }
            .catch { exception ->
                if (exception is CancellationException) throw exception
                _effects.send(
                    CollectionEffect.ShowError(
                        exception.message ?: "Failed to load collection"
                    )
                )
                emit(ContentUpdate(isLoading = false))
            }
    }

    private fun observePreferences() {
        viewModelScope.launch {
            preferencesFlow()
                .distinctUntilChanged()
                .catch { exception ->
                    if (exception is CancellationException) throw exception
                    _effects.send(
                        CollectionEffect.ShowError(
                            exception.message ?: "Failed to load preferences"
                        )
                    )
                }
                .collect { preferences ->
                    _uiState.update { it.copy(listTypeState = preferences) }
                }
        }
    }

    private suspend fun readPreferences() : ListTypeState =
        preferencesFlow()
            .catch { exception ->
                if (exception is CancellationException) throw exception
                emit(defaultPreferences())
            }
            .first()

    private fun defaultPreferences() = ListTypeState(
        view = ListViewType.GRID,
        sort = Sort.LATEST,
        list = ListType.ALBUM
    )

    private fun preferencesFlow() : Flow<ListTypeState> = combine(
        repository.getSelectedViewOption(),
        repository.getSelectedListOption(),
        repository.getSelectedSortOption()
    ) { view, list, sort ->
        ListTypeState(view = view, sort = sort, list = list)
    }

    private fun applyContentUpdate(update : ContentUpdate) {
        _uiState.update { current ->
            current.copy(
                isLoading = update.isLoading ?: current.isLoading,
                albums = update.albums ?: current.albums,
                artists = update.artists ?: current.artists,
                lyrics = update.lyrics ?: current.lyrics
            )
        }
    }

    private inline fun reduce(
        transform : CollectionUiState.() -> CollectionUiState
    ) {
        _uiState.update { it.transform() }
    }

    private fun persist(block : suspend () -> Unit) {
        viewModelScope.launch {
            try {
                block()
            } catch (e : CancellationException) {
                throw e
            } catch (e : Exception) {
                _effects.send(
                    CollectionEffect.ShowError(e.message ?: "Failed to save preferences")
                )
            }
        }
    }
}