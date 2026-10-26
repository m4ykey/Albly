package com.m4ykey.artist.domain.use_case

import com.m4ykey.artist.data.local.ArtistListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetSavedArtistUseCase {
    operator fun invoke(query : String) : Flow<List<ArtistListItem>> {
        return flowOf(emptyList())
    }
}