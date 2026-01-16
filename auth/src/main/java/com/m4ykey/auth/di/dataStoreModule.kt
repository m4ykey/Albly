package com.m4ykey.auth.di

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val AUTH_DATASTORE_NAME = "auth_prefs"

val dataStoreModule = module {
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler { exception ->
                Log.e("DataStore", "DataStore Auth corruption error", exception)
                emptyPreferences()
            },
            scope = get(named("ApplicationScope")),
            produceFile = { androidContext().preferencesDataStoreFile(AUTH_DATASTORE_NAME) }
        )
    }
}