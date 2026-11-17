package com.m4ykey.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.m4ykey.core.database.model.search.SearchItem
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertSearchItem(search : SearchItem)

    @Query("SELECT * FROM search_item_table ORDER BY id DESC LIMIT :limit")
    fun getSearchItemList(limit : Int) : Flow<List<SearchItem>>

    @Query("DELETE FROM search_item_table")
    suspend fun deleteAllSearchItems()

    @Delete
    suspend fun deleteSearchItem(search : SearchItem)

    @Query("""
        DELETE FROM search_item_table
        WHERE id NOT IN (
            SELECT id FROM search_item_table ORDER BY id DESC LIMIT :limit
        )
    """)
    suspend fun pruneSearchItems(limit: Int)

}