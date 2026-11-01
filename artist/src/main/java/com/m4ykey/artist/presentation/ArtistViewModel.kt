@file:OptIn(ExperimentalCoroutinesApi::class)

package com.m4ykey.artist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.m4ykey.artist.domain.model.ReleaseItem
import com.m4ykey.artist.domain.repository.ArtistRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class ArtistViewModel(
    private val repository : ArtistRepository
) : ViewModel() {

    private val _artistDialogState = MutableStateFlow<ArtistState?>(null)
    val artistDialogState = _artistDialogState.asStateFlow()

    fun loadArtists(artists : List<Int>) {
        viewModelScope.launch {
            _artistDialogState.value = ArtistState.Loading

            _artistDialogState.value = try {
                val result = artists.map { id ->
                    async { repository.getArtistById(id).first() }
                }.awaitAll()

                ArtistState.Success(result)
            } catch (e : Exception) {
                ArtistState.Error(e.message ?: "Failed to fetch artists")
            }
        }
    }

    private val releaseId = MutableStateFlow<Int?>(null)

    private val releaseFlow : Flow<PagingData<ReleaseItem>> = releaseId
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { id ->
            repository.getArtistReleases(id)
        }
        .cachedIn(viewModelScope)

    fun getArtistReleases() : Flow<PagingData<ReleaseItem>> = releaseFlow

    fun setReleaseId(id : Int) {
        releaseId.value = id
    }

}