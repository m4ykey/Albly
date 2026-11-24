package com.m4ykey.artist.domain.repository

import androidx.paging.PagingData
import com.m4ykey.artist.domain.model.Artist
import com.m4ykey.artist.domain.model.ReleaseItem
import com.m4ykey.core.database.model.artist.ArtistEntity
import com.m4ykey.core.database.model.artist.ArtistListItem
import com.m4ykey.core.database.model.artist.ArtistWithStates
import com.m4ykey.core.database.model.artist.IsArtistSaved
import kotlinx.coroutines.flow.Flow

interface ArtistRepository {

    suspend fun getArtistById(id : Int) : Flow<Artist>
    fun getArtistReleases(id : Int) : Flow<PagingData<ReleaseItem>>

    suspend fun insertArtist(artist : ArtistEntity)
    suspend fun insertSavedArtist(isArtistSaved: IsArtistSaved)
    fun getSavedArtistState(id : Int) : IsArtistSaved?
    suspend fun getArtistWithStates(id : Int) : ArtistWithStates?
    suspend fun deleteArtist(id : Int)
    suspend fun deleteSavedArtistState(id : Int)
    fun getSavedArtistLatest(query : String) : Flow<List<ArtistListItem>>
    fun getSavedArtistOldest(query : String) : Flow<List<ArtistListItem>>
    fun getSavedArtistAlphabetical(query : String) : Flow<List<ArtistListItem>>
    suspend fun cacheArtist(artist : ArtistEntity)

}