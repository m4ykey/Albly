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

    @Query("SELECT * FROM lyrics_table WHERE id = :id")
    fun getLyricsById(id : Int) : LyricsEntity?

    @Query("select * from is_lyrics_saved_table where track_key = :trackKey")
    fun getSavedLyricsState(trackKey : String) : IsLyricsSaved?

    @Transaction
    @Query("select * from lyrics_table where id = :id")
    suspend fun getLyricsWithState(id : Int) : LyricsWithState?

    @Query("delete from lyrics_table where id = :id")
    suspend fun deleteLyrics(id : Int)

    @Query("delete from is_lyrics_saved_table where track_key = :trackKey")
    suspend fun deleteSavedLyricsState(trackKey: String)

    @Query("""
        select lyrics_table.*
        from lyrics_table
        inner join is_lyrics_saved_table
            on lyrics_table.track_key = is_lyrics_saved_table.track_key
        where is_lyrics_saved_table.isLyricsSaved = 1
        and (
            lyrics_table.title LIKE '%' || :query || '%'
            or lyrics_table.artist LIKE '%' || :query || '%'
        )
        order by lyrics_table.save_time DESC
    """)
    fun getSavedLyricsLatest(query : String) : Flow<List<LyricsEntity>>

    @Query("""
        select lyrics_table.*
        from lyrics_table
        inner join is_lyrics_saved_table
            on lyrics_table.track_key = is_lyrics_saved_table.track_key
        where is_lyrics_saved_table.isLyricsSaved = 1
        and (
            lyrics_table.title LIKE '%' || :query || '%'
            or lyrics_table.artist LIKE '%' || :query || '%'
        )
        order by lyrics_table.save_time ASC
    """)
    fun getSavedLyricsOldest(query : String) : Flow<List<LyricsEntity>>

    @Query("""
        select lyrics_table.*
        from lyrics_table
        inner join is_lyrics_saved_table
            on lyrics_table.track_key = is_lyrics_saved_table.track_key
        where is_lyrics_saved_table.isLyricsSaved = 1
        and (
            lyrics_table.title LIKE '%' || :query || '%'
            or lyrics_table.artist LIKE '%' || :query || '%'
        )
        order by lyrics_table.title ASC
    """)
    fun getSavedLyricsAlphabetical(query : String) : Flow<List<LyricsEntity>>

}