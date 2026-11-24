package com.m4ykey.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.m4ykey.core.database.model.artist.ArtistEntity
import com.m4ykey.core.database.model.artist.ArtistListItem
import com.m4ykey.core.database.model.artist.ArtistWithStates
import com.m4ykey.core.database.model.artist.IsArtistSaved
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertArtist(artist : ArtistEntity)

    @Insert(onConflict = REPLACE)
    suspend fun insertSavedArtist(isArtistSaved: IsArtistSaved)

    @Query("SELECT * FROM is_artist_saved_table WHERE id = :id")
    fun getSavedArtistState(id : Int) : IsArtistSaved?

    @Transaction
    @Query("SELECT * FROM artist_table WHERE id = :id")
    suspend fun getArtistWithStates(id : Int) : ArtistWithStates?

    @Query("DELETE FROM artist_table WHERE id = :id")
    suspend fun deleteArtist(id : Int)

    @Query("SELECT * FROM is_artist_saved_table WHERE id = :id")
    suspend fun deleteSavedArtistState(id : Int)

    @Query("""
        SELECT 
            artist_table.id,
            artist_table.realname,
            artist_table.images
        FROM artist_table
        INNER JOIN is_artist_saved_table ON artist_table.id = is_artist_saved_table.id
        WHERE is_artist_saved_table.isArtistSaved = 1
        AND (
            artist_table.realname LIKE '%' || :query || '%'
        )
        ORDER BY artist_table.save_time DESC
    """)
    fun getSavedArtistLatest(query : String) : Flow<List<ArtistListItem>>

    @Query("""
        SELECT 
            artist_table.id,
            artist_table.realname,
            artist_table.images
        FROM artist_table
        INNER JOIN is_artist_saved_table ON artist_table.id = is_artist_saved_table.id
        WHERE is_artist_saved_table.isArtistSaved = 1
        AND (
            artist_table.realname LIKE '%' || :query || '%'
        )
        ORDER BY artist_table.save_time ASC
    """)
    fun getSavedArtistOldest(query : String) : Flow<List<ArtistListItem>>

    @Query("""
        SELECT 
            artist_table.id,
            artist_table.realname,
            artist_table.images
        FROM artist_table
        INNER JOIN is_artist_saved_table ON artist_table.id = is_artist_saved_table.id
        WHERE is_artist_saved_table.isArtistSaved = 1
        AND (
            artist_table.realname LIKE '%' || :query || '%'
        )
        ORDER BY artist_table.realname ASC
    """)
    fun getSavedArtistAlphabetical(query : String) : Flow<List<ArtistListItem>>

    @Query("""
        SELECT COUNT(*) FROM artist_table
        WHERE id NOT IN (SELECT id FROM is_artist_saved_table)
    """)
    suspend fun countCachedArtists() : Int

    @Query("""
        DELETE FROM artist_table
        WHERE id IN (
            SELECT id FROM artist_table
            WHERE id NOT IN (SELECT id FROM is_artist_saved_table)
            ORDER BY save_time ASC
            LIMIT :limit
        )
    """)
    suspend fun deleteOldestCachedArtists(limit : Int)

    @Query("SELECT EXISTS(SELECT 1 FROM artist_table WHERE id = :id)")
    suspend fun isArtistStored(id : Int) : Boolean

}