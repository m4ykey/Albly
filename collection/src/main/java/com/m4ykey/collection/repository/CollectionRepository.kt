package com.m4ykey.collection.repository

import com.m4ykey.album.data.local.model.AlbumSort
import com.m4ykey.collection.presentation.type.ListType
import com.m4ykey.collection.presentation.type.ListViewType
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {

    suspend fun saveViewOption(view : ListViewType)
    suspend fun saveListOption(list: ListType)
    suspend fun saveSortOption(sort : AlbumSort)

    fun getSelectedListOption() : Flow<ListType>
    fun getSelectedSortOption() : Flow<AlbumSort>
    fun getSelectedViewOption() : Flow<ListViewType>

}