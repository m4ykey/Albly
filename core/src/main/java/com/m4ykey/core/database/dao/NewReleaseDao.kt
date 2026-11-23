package com.m4ykey.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.m4ykey.core.database.model.album.NewReleaseCursor
import com.m4ykey.core.database.model.album.NewReleaseEntity

@Dao
interface NewReleaseDao {

    @Query("select * from new_release_table order by position ASC")
    fun pagingSource() : PagingSource<Int, NewReleaseEntity>

    @Query("select * from new_release_cursor where id = 0")
    suspend fun cursor() : NewReleaseCursor?

    @Upsert
    suspend fun upsertCursor(cursor: NewReleaseCursor)

    @Insert(onConflict = REPLACE)
    suspend fun insertItems(items : List<NewReleaseEntity>)

    @Query("delete from new_release_table")
    suspend fun clearItems()

    @Transaction
    suspend fun replaceItems(items : List<NewReleaseEntity>, cursor: NewReleaseCursor) {
        clearItems()
        insertItems(items)
        upsertCursor(cursor)
    }

    @Query("select count(*) from new_release_table")
    suspend fun countItems() : Int

}