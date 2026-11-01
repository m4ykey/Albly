package com.m4ykey.artist.domain.repository

import androidx.paging.PagingData
import com.m4ykey.artist.domain.model.Artist
import com.m4ykey.artist.domain.model.ReleaseItem
import kotlinx.coroutines.flow.Flow

interface ArtistRepository {

    suspend fun getArtistById(id : Int) : Flow<Artist>
    fun getArtistReleases(id : Int) : Flow<PagingData<ReleaseItem>>

}