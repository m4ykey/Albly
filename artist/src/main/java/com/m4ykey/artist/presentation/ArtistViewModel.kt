@file:OptIn(ExperimentalCoroutinesApi::class)

package com.m4ykey.artist.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.m4ykey.artist.data.mapper.toArtist
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
        _uiState.update {
            it.copy(
                artistId = id.takeIf { id -> id > 0 },
                isSaved = false
            )
        }
    }

    private suspend fun fetchArtists(ids : List<Int>) : Result<List<Artist>> {
        return try {
            val artists = coroutineScope {
                ids
                    .map { id -> async { repository.getArtistById(id).first() } }
                    .awaitAll()
            }

            artists.forEach(::cacheArtist)
            Result.success(artists)
        } catch (e : CancellationException) {
            throw e
        } catch (e : Exception) {
            Result.failure(e)
        }
    }

    private suspend fun fetchCachedArtists(ids : List<Int>) : List<Artist> {
        return ids.mapNotNull { id ->
            try {
                val withStates = repository.getArtistWithStates(id)
                withStates?.artistEntity?.toArtist()
            } catch (e : CancellationException) {
                throw e
            } catch (_ : Exception) {
                null
            }
        }
    }

    private suspend fun isArtistSaved(id : Int?) : Boolean {
        return id != null && try {
            repository.getSavedArtistState(id) != null
        } catch (e : CancellationException) {
            throw e
        } catch (_ : Exception) {
            false
        }
    }

    private fun loadArtists(ids : List<Int>) {
        val validIds = ids.filter { it > 0 }
        if (validIds.isEmpty()) {
            _uiState.update {
                it.copy(artistState = ArtistState.Error("Invalid artist id"))
            }
            return
        }

        artistJob?.cancel()
        artistJob = viewModelScope.launch {
            val id = _uiState.value.artistId
            val cached = fetchCachedArtists(validIds)

            if (cached.isNotEmpty()) {
                _uiState.update {
                    it.copy(
                        isSaved = isArtistSaved(id),
                        artistState = ArtistState.Success(cached)
                    )
                }
            } else {
                _uiState.update { it.copy(artistState = ArtistState.Loading) }
            }

            fetchArtists(validIds).fold(
                onSuccess = { artists ->
                    _uiState.update {
                        it.copy(
                            artistState = ArtistState.Success(artists),
                            isSaved = isArtistSaved(id)
                        )
                    }
                },
                onFailure = { error ->
                    if (cached.isEmpty()) {
                        _uiState.update {
                            it.copy(
                                artistState = ArtistState.Error(
                                    error.message ?: "Failed to fetch artist"
                                )
                            )
                        }
                    }
                }
            )
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
                fetchArtists(listOf(id)).fold(
                    onSuccess = { artists ->
                        _uiState.update {
                            it.copy(
                                artistState = ArtistState.Success(artists),
                                isSaved = isArtistSaved(id)
                            )
                        }
                    },
                    onFailure = { error ->
                        if (_uiState.value.artistState !is ArtistState.Success) {
                            _uiState.update {
                                it.copy(
                                    artistState = ArtistState.Error(
                                        error.message ?: "Failed to fetch artist"
                                    )
                                )
                            }
                        } else {
                            _effects.send(ArtistEffect.ShowError("Offline — showing cached data"))
                        }
                    }
                )
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

        val id = current.artistId ?: return
        val success = current.artistState as? ArtistState.Success ?: return
        val artist = success.artists.firstOrNull { it.id == id } ?: return

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