package com.m4ykey.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.m4ykey.core.database.model.artist.ArtistReleaseCursor
import com.m4ykey.core.database.model.artist.ArtistReleaseEntity

@Dao
interface ArtistReleaseDao {

    @Transaction
    suspend fun replaceItems(
        artistId : Int,
        items : List<ArtistReleaseEntity>,
        cursor : ArtistReleaseCursor
    ) {
        clearItems(artistId)
        insertItems(items)
        upsertCursor(cursor)
    }

    @Query("select * from artist_release_entity where artistId = :artistId order by position asc")
    fun pagingSource(artistId : Int) : PagingSource<Int, ArtistReleaseEntity>

    @Query("select * from artist_release_cursor where artistId = :artistId")
    suspend fun cursor(artistId : Int) : ArtistReleaseCursor?

    @Query("select count(*) from artist_release_entity where artistId = :artistId")
    suspend fun countItems(artistId : Int) : Int

    @Upsert
    suspend fun upsertCursor(cursor : ArtistReleaseCursor)

    @Insert(onConflict = REPLACE)
    suspend fun insertItems(items : List<ArtistReleaseEntity>)

    @Query("delete from artist_release_entity where artistId = :artistId")
    suspend fun clearItems(artistId : Int)

}