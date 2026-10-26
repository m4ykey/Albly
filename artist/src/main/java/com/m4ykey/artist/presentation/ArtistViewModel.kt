package com.m4ykey.artist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.artist.domain.model.Artist
import com.m4ykey.artist.domain.repository.ArtistRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ArtistViewModel(
    private val repository : ArtistRepository
) : ViewModel() {

    private val _artistDialogState = MutableStateFlow<ArtistDialogState?>(null)
    val artistDialogState = _artistDialogState.asStateFlow()

    fun loadArtists(artists : List<Int>) {
        viewModelScope.launch {
            _artistDialogState.value = ArtistDialogState.Loading

            _artistDialogState.value = try {
                val result = artists.map { id ->
                    async { repository.getArtistById(id).first() }
                }.awaitAll()

                ArtistDialogState.Success(result)
            } catch (e : Exception) {
                ArtistDialogState.Error(e.message ?: "Failed to fetch artists")
            }
        }
    }

}