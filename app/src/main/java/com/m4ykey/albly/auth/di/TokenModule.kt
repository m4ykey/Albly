package com.m4ykey.albly.auth.di

import com.m4ykey.albly.auth.token.SpotiTokenProvider
import com.m4ykey.albly.auth.token.TokenHeaderProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import kotlinx.coroutines.runBlocking

@Module
@InstallIn(SingletonComponent::class)
object TokenModule {

    @Provides
    @Singleton
    fun provideTokenHeaderProvider(
        spotiTokenProvider: SpotiTokenProvider
    ) : TokenHeaderProvider = object : TokenHeaderProvider {
        override fun getAuthorizationToken(): String? {
            return runBlocking { spotiTokenProvider.getAccessToken() }
        }
    }

}