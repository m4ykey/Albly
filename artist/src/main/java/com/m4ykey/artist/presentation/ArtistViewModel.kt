@file:OptIn(ExperimentalCoroutinesApi::class)

package com.m4ykey.artist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.m4ykey.artist.domain.model.ReleaseItem
import com.m4ykey.artist.domain.repository.ArtistRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ArtistViewModel(
    private val repository : ArtistRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ArtistUiState())
    val uiState : StateFlow<ArtistUiState> = _uiState.asStateFlow()

    private var artistJob : Job? = null

    fun onAction(action: ArtistAction) {
        when (action) {
            is ArtistAction.OnLoadArtist -> {
                setArtistId(action.id)
                loadArtists(listOf(action.id))
            }
            is ArtistAction.OnLoadArtists -> loadArtists(action.ids)
        }
    }

    val artistRelease : Flow<PagingData<ReleaseItem>> = _uiState
        .mapNotNull { it.artistId }
        .distinctUntilChanged()
        .flatMapLatest { id -> repository.getArtistReleases(id) }
        .cachedIn(viewModelScope)

    private fun setArtistId(id : Int) {
        _uiState.update { it.copy(artistId = id) }
    }

    private fun loadArtists(ids : List<Int>) {
        if (ids.isEmpty()) return

        artistJob?.cancel()
        artistJob = viewModelScope.launch {
            _uiState.update { it.copy(artistState = ArtistState.Loading) }

            val state = try {
                val result = ids
                    .map { id -> async { repository.getArtistById(id).first() } }
                    .awaitAll()

                ArtistState.Success(result)
            } catch (e : Exception) {
                ArtistState.Error(e.message ?: "Failed to fetch artists")
            }

            _uiState.update { it.copy(artistState = state) }
        }
    }
}