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
    abstract fun pagingSource() : PagingSource<Int, NewReleaseEntity>

    @Query("select * from new_release_cursor where id = 0")
    abstract suspend fun cursor() : NewReleaseCursor?

    @Upsert
    abstract suspend fun upsertCursor(cursor: NewReleaseCursor)

    @Insert(onConflict = REPLACE)
    abstract suspend fun insertItems(items : List<NewReleaseEntity>)

    @Query("delete from new_release_table")
    abstract suspend fun clearItems()

    @Transaction
    open suspend fun replaceItems(items : List<NewReleaseEntity>, cursor: NewReleaseCursor) {
        clearItems()
        insertItems(items)
        upsertCursor(cursor)
    }

    @Query("select count(*) from new_release_table")
    abstract suspend fun countItems() : Int

}