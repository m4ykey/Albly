package com.m4ykey.album.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.m4ykey.album.data.local.model.AlbumEntity
import com.m4ykey.album.data.local.model.AlbumWithStates
import com.m4ykey.album.data.local.model.IsAlbumSaved
import com.m4ykey.album.data.local.model.IsListenLaterSaved
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertAlbum(album : AlbumEntity)

    @Insert(onConflict = REPLACE)
    suspend fun insertSavedAlbum(isAlbumSaved: IsAlbumSaved)

    @Insert(onConflict = REPLACE)
    suspend fun insertListenLaterSaved(isListenLaterSaved: IsListenLaterSaved)

    @Query("SELECT * FROM album_table WHERE id = :id")
    fun getAlbumById(id : String) : AlbumEntity?

    @Query("SELECT * FROM is_album_saved_table WHERE id = :id")
    fun getSavedAlbumState(id : String) : IsAlbumSaved?

    @Query("SELECT * FROM is_listen_later_saved_table WHERE id = :id")
    fun getSavedListenLaterState(id : String) : IsListenLaterSaved?

    @Transaction
    @Query("SELECT * FROM album_table WHERE id = :id")
    suspend fun getAlbumWithStates(id : String) : AlbumWithStates?

    @Query("DELETE FROM album_table WHERE id = :id")
    suspend fun deleteAlbum(id : String)

    @Query("DELETE FROM is_listen_later_saved_table WHERE id = :id")
    suspend fun deleteSavedListenLaterState(id : String)

    @Query("DELETE FROM is_album_saved_table WHERE id = :id")
    suspend fun deleteSavedAlbumState(id : String)

    @Query("""
        SELECT album_table.* FROM album_table
        INNER JOIN is_album_saved_table ON album_table.id = is_album_saved_table.id
        WHERE is_album_saved_table.isAlbumSaved = 1
        AND (album_table.name LIKE '%' || :query || '%')
        AND (:type IS NULL OR album_table.album_type = :type)
        ORDER BY save_time DESC
    """)
    fun getSavedAlbums(query : String, type : String?) : Flow<List<AlbumEntity>>

    @Query("""
        SELECT album_table.* FROM album_table
        INNER JOIN is_listen_later_saved_table ON album_table.id = is_listen_later_saved_table.id
        WHERE is_listen_later_saved_table.isListenLater = 1 ORDER BY name ASC
    """)
    suspend fun getListenLaterAlbums() : List<AlbumEntity>

    @Query("""
        SELECT album_table.* FROM album_table
        INNER JOIN is_listen_later_saved_table ON album_table.id = is_listen_later_saved_table.id
        WHERE is_listen_later_saved_table.isListenLater = 1 ORDER BY RANDOM() LIMIT 1
    """)
    fun getRandomAlbum() : Flow<AlbumEntity>

}