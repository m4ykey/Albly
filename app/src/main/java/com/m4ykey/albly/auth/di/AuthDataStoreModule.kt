package com.m4ykey.albly.auth.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.m4ykey.albly.di.SpotiDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val SPOTI_PREFERENCES = "spoti_preferences"

@Module
@InstallIn(SingletonComponent::class)
object AuthDataStoreModule {

    @Singleton
    @Provides
    @SpotiDataStore
    fun provideSpotiDataStore(
        @ApplicationContext context: Context
    ) : DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = null,
            migrations = emptyList(),
            produceFile = { context.preferencesDataStoreFile(SPOTI_PREFERENCES) }
        )
    }

}