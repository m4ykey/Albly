package com.m4ykey.collection.repository

import com.m4ykey.collection.presentation.type.ListType
import com.m4ykey.collection.presentation.type.ListViewType
import com.m4ykey.core.Sort
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {

    suspend fun saveViewOption(view : ListViewType)
    suspend fun saveListOption(list: ListType)
    suspend fun saveSortOption(sort : Sort)

    fun getSelectedListOption() : Flow<ListType>
    fun getSelectedSortOption() : Flow<Sort>
    fun getSelectedViewOption() : Flow<ListViewType>

}