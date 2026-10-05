package com.m4ykey.collection.repository

import com.m4ykey.album.data.local.model.AlbumSort
import com.m4ykey.collection.preferences.CollectionPreferences
import com.m4ykey.collection.presentation.type.ListType
import com.m4ykey.collection.presentation.type.ListViewType
import kotlinx.coroutines.flow.Flow

class CollectionRepositoryImpl(
    private val pref : CollectionPreferences
) : CollectionRepository {

    override suspend fun saveViewOption(view: ListViewType) {
        return pref.saveViewOption(view)
    }

    override suspend fun saveListOption(list: ListType) {
        return pref.saveListOption(list)
    }

    override suspend fun saveSortOption(sort: AlbumSort) {
        return pref.saveSortOption(sort)
    }

    override fun getSelectedListOption(): Flow<ListType> {
        return pref.getSelectedListOption()
    }

    override fun getSelectedSortOption(): Flow<AlbumSort> {
        return pref.getSelectedSortOption()
    }

    override fun getSelectedViewOption(): Flow<ListViewType> {
        return pref.getSelectedViewOption()
    }
}