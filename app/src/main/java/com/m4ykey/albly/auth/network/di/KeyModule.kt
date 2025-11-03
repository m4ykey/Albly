package com.m4ykey.albly.auth.network.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val SPOTIFY_KEY = "spotify_key"

@Module
@InstallIn(SingletonComponent::class)
object KeyModule {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = SPOTIFY_KEY)

    @Provides
    @Singleton
    fun provideSpotifyDataStore(@ApplicationContext context: Context) : DataStore<Preferences> = context.dataStore
}