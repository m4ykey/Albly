@file:OptIn(ExperimentalCoroutinesApi::class)

package com.m4ykey.artist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.m4ykey.artist.data.mapper.toArtistEntity
import com.m4ykey.artist.domain.model.Artist
import com.m4ykey.artist.domain.model.ReleaseItem
import com.m4ykey.artist.domain.repository.ArtistRepository
import com.m4ykey.artist.domain.use_case.CacheArtistUseCase
import com.m4ykey.artist.domain.use_case.ToggleArtistSavedUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ArtistViewModel(
    private val repository : ArtistRepository,
    private val cacheArtistUseCase : CacheArtistUseCase,
    private val toggleArtistSavedUseCase: ToggleArtistSavedUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ArtistUiState())
    val uiState : StateFlow<ArtistUiState> = _uiState.asStateFlow()

    private val _effects = Channel<ArtistEffect>(Channel.BUFFERED)
    val effects : Flow<ArtistEffect> = _effects.receiveAsFlow()

    private var artistJob : Job? = null

    fun onAction(action: ArtistAction) {
        when (action) {
            is ArtistAction.OnLoadArtist -> {
                setArtistId(action.id)
                loadArtists(listOf(action.id))
            }
            is ArtistAction.OnLoadArtists -> loadArtists(action.ids)
            ArtistAction.OnRefresh -> refresh()
            ArtistAction.OnToggleSave -> toggleSave()
        }
    }

    val artistRelease : Flow<PagingData<ReleaseItem>> = _uiState
        .mapNotNull { it.artistId }
        .distinctUntilChanged()
        .flatMapLatest { id -> repository.getArtistReleases(id) }
        .cachedIn(viewModelScope)

    private fun setArtistId(id : Int) {
        _uiState.update { it.copy(artistId = id, isSaved = false) }
    }

    private suspend fun fetchArtists(ids : List<Int>) : List<Artist> {
        return coroutineScope {
            ids
                .map { id -> async { repository.getArtistById(id).first() } }
                .awaitAll()
        }
    }

    private fun loadArtists(ids : List<Int>) {
        if (ids.isEmpty()) return

        artistJob?.cancel()
        artistJob = viewModelScope.launch {
            _uiState.update { it.copy(artistState = ArtistState.Loading) }

            val state = try {
                ArtistState.Success(fetchArtists(ids))
            } catch (e : CancellationException) {
                throw e
            } catch (e : Exception) {
                ArtistState.Error(e.message ?: "Failed to fetch artist")
            }

            val isSaved = if (state is ArtistState.Success) {
                val id = _uiState.value.artistId
                id != null && repository.getSavedArtistState(id) != null
            } else {
                false
            }

            _uiState.update { it.copy(artistState = state, isSaved = isSaved) }
        }
    }

    private fun refresh() {
        val current = _uiState.value
        val id = current.artistId ?: return
        if (current.isRefreshing || current.artistState == ArtistState.Loading) return

        artistJob?.cancel()
        artistJob = viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            try {
                val artists = fetchArtists(listOf(id))
                _uiState.update { it.copy(artistState = ArtistState.Success(artists)) }
            } catch (e : CancellationException) {
                throw e
            } catch (e : Exception) {
                _uiState.update { state ->
                    if (state.artistState is ArtistState.Success) state
                    else state.copy(
                        artistState = ArtistState.Error(e.message ?: "Failed to fetch artists")
                    )
                }
            } finally {
                _uiState.update { it.copy(isRefreshing = false) }
            }
        }
    }

    private fun cacheArtist(artist : Artist) {
        viewModelScope.launch {
            try {
                cacheArtistUseCase(artist.toArtistEntity())
            } catch (exception : CancellationException) {
                throw exception
            } catch (_ : Exception) {

            }
        }
    }

    private fun toggleSave() {
        val current = _uiState.value
        if (current.isSaving) return

        val success = current.artistState as? ArtistState.Success ?: return
        val artist = success.artists.firstOrNull { it.id == current.artistId }
            ?: success.artists.firstOrNull()
            ?: return

        val wasSaved = current.isSaved

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            try {
                toggleArtistSavedUseCase(
                    isCurrentlySaved = wasSaved,
                    artist = artist.toArtistEntity()
                )
                _uiState.update { it.copy(isSaved = !wasSaved) }
            } catch (e : CancellationException) {
                throw e
            } catch (e : Exception) {
                _effects.send(
                    ArtistEffect.ShowError(
                        e.message ?: "Failed to update saved state"
                    )
                )
            } finally {
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }
}