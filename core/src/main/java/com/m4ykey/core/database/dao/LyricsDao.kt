package com.m4ykey.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.m4ykey.core.database.model.lyrics.IsLyricsSaved
import com.m4ykey.core.database.model.lyrics.LyricsEntity
import com.m4ykey.core.database.model.lyrics.LyricsWithState
import kotlinx.coroutines.flow.Flow

@Dao
interface LyricsDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertLyrics(lyrics : LyricsEntity)

    @Insert(onConflict = REPLACE)
    suspend fun insertSavedLyrics(isLyricsSaved: IsLyricsSaved)

    @Query("""
        select * from lyrics_table
        where (
            (:remoteId is not null and :remoteId > 0 and remote_id = :remoteId)
            or (:isrc is not null and :isrc != '' and isrc = :isrc)
            or (:track is not null and :artist is not null and lower(title) = lower(:track) and lower(artist) = lower(:artist))
        )
        and (:source is null or :source = '' or source = :source)
        limit 1
    """)
    fun getLyricsById(
        source : String? = null,
        remoteId : Int? = null,
        isrc : String? = null,
        track : String? = null,
        artist : String? = null
    ) : LyricsEntity?

    @Query("""
        select * from is_lyrics_saved_table
        where (:source = '' or source = :source)
        and (
            (:remoteId > 0 and remote_id = :remoteId)
            or (:remoteId <= 0 and :isrc is not null and isrc = :isrc)
        )
        limit 1
    """)
    fun getSavedLyricsState(source : String, remoteId : Int, isrc : String?) : IsLyricsSaved?

    @Transaction
    @Query("""
        select * from lyrics_table
        where (
            (:remoteId is not null and :remoteId > 0 and remote_id = :remoteId)
            or (:isrc is not null and :isrc != '' and isrc = :isrc)
            or (:track is not null and :artist is not null and lower(title) = lower(:track) and lower(artist) = lower(:artist))
        )
        and (:source is null or :source = '' or source = :source)
        limit 1
    """)
    suspend fun getLyricsWithState(
        source : String? = null,
        remoteId : Int? = null,
        isrc : String? = null,
        track : String? = null,
        artist: String? = null
    ) : LyricsWithState?

    @Query("""
        delete from lyrics_table
        where source = :source
        and (
            (:remoteId > 0 and remote_id = :remoteId)
            or (:remoteId <= 0 and :isrc is not null and isrc = :isrc)
        )
    """)
    suspend fun deleteLyrics(source : String, remoteId : Int, isrc : String?)

    @Query("""
        delete from is_lyrics_saved_table
        where (:source = '' or source = :source)
        and (
            (:remoteId > 0 and remote_id = :remoteId)
            or (:remoteId <= 0 and :isrc is not null and isrc = :isrc)
        )
    """)
    suspend fun deleteSavedLyricsState(source: String, remoteId : Int, isrc : String?)

    @Query("""
        select lyrics_table.*
        from lyrics_table
        inner join is_lyrics_saved_table
            on (
                (lyrics_table.remote_id > 0 and lyrics_table.remote_id = is_lyrics_saved_table.remote_id)
                or (lyrics_table.remote_id <= 0 and lyrics_table.isrc is not null and lyrics_table.isrc = is_lyrics_saved_table.isrc)
            )
        where is_lyrics_saved_table.isLyricsSaved = 1
        and (
            lyrics_table.title LIKE '%' || :query || '%'
            or lyrics_table.artist LIKE '%' || :query || '%'
        )
        order by lyrics_table.save_time desc
    """)
    fun getSavedLyricsLatest(query : String) : Flow<List<LyricsEntity>>

    @Query("""
        select lyrics_table.*
        from lyrics_table
        inner join is_lyrics_saved_table
            on (
                (lyrics_table.remote_id > 0 and lyrics_table.remote_id = is_lyrics_saved_table.remote_id)
                or (lyrics_table.remote_id <= 0 and lyrics_table.isrc is not null and lyrics_table.isrc = is_lyrics_saved_table.isrc)
            )
        where is_lyrics_saved_table.isLyricsSaved = 1
        and (
            lyrics_table.title LIKE '%' || :query || '%'
            or lyrics_table.artist LIKE '%' || :query || '%'
        )
        order by lyrics_table.save_time asc
    """)
    fun getSavedLyricsOldest(query : String) : Flow<List<LyricsEntity>>

    @Query("""
        select lyrics_table.*
        from lyrics_table
        inner join is_lyrics_saved_table
            on (
                (lyrics_table.remote_id > 0 and lyrics_table.remote_id = is_lyrics_saved_table.remote_id)
                or (lyrics_table.remote_id <= 0 and lyrics_table.isrc is not null and lyrics_table.isrc = is_lyrics_saved_table.isrc)
            )
        where is_lyrics_saved_table.isLyricsSaved = 1
        and (
            lyrics_table.title LIKE '%' || :query || '%'
            or lyrics_table.artist LIKE '%' || :query || '%'
        )
        order by lyrics_table.title asc
    """)
    fun getSavedLyricsAlphabetical(query : String) : Flow<List<LyricsEntity>>

}