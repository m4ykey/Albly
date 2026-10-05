package com.m4ykey.collection.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.m4ykey.album.data.local.model.AlbumSort
import com.m4ykey.collection.presentation.type.ListType
import com.m4ykey.collection.presentation.type.ListViewType
import com.m4ykey.core.datastore.safeDataStoreOperations
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class CollectionPreferences(
    private val dataStore : DataStore<Preferences>
) {

    companion object {
        private val SELECTED_SORT_KEY = stringPreferencesKey("selected_sort")
        private val SELECTED_LIST_KEY = stringPreferencesKey("selected_list")
        private val SELECTED_VIEW_KEY = stringPreferencesKey("selected_view")
    }

    suspend fun saveViewOption(view : ListViewType) {
        safeDataStoreOperations {
            dataStore.edit { pref ->
                pref[SELECTED_VIEW_KEY] = view.name
            }
        }
    }

    suspend fun saveListOption(list : ListType) {
        safeDataStoreOperations {
            dataStore.edit { pref ->
                pref[SELECTED_LIST_KEY] = list.name
            }
        }
    }

    suspend fun saveSortOption(sort : AlbumSort) {
        safeDataStoreOperations {
            dataStore.edit { pref ->
                pref[SELECTED_SORT_KEY] = sort.name
            }
        }
    }

    fun getSelectedListOption() : Flow<ListType> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { pref ->
                val list = pref[SELECTED_LIST_KEY]

                list?.let {
                    ListType.valueOf(it)
                } ?: ListType.ALBUM
            }
    }

    fun getSelectedSortOption() : Flow<AlbumSort> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { pref ->
                val sort = pref[SELECTED_SORT_KEY]

                sort?.let {
                    AlbumSort.valueOf(it)
                } ?: AlbumSort.LATEST
            }
    }

    fun getSelectedViewOption() : Flow<ListViewType> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { pref ->
                val view = pref[SELECTED_VIEW_KEY]

                view?.let {
                    ListViewType.valueOf(it)
                } ?: ListViewType.GRID
            }
    }
}