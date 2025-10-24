package com.m4ykey.albly.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @AuthBaseUrl
    fun provideAuthBaseUrl() : String = "https://accounts.spotify.com/"

    @Provides
    @Singleton
    @SpotBaseUrl
    fun provideSpotBaseUrl() : String = "https://api.spotify.com/v1/"

    @Provides
    @Singleton
    fun provideMoshi() : Moshi {
        return Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

}