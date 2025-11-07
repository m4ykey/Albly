package com.m4ykey.albly.album.di

import android.util.Log
import com.m4ykey.albly.album.data.network.service.AlbumService
import com.m4ykey.albly.auth.token.CustomTokenProvider
import com.m4ykey.albly.di.AlbumRetrofit
import com.m4ykey.albly.di.SpotBaseUrl
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AlbumNetworkModule {

    @Provides
    @Singleton
    fun provideAlbumService(@AlbumRetrofit retrofit: Retrofit) : AlbumService {
        return retrofit.create(AlbumService::class.java)
    }

    @Provides
    @Singleton
    @AlbumRetrofit
    fun provideAlbumRetrofit(
        moshi : Moshi,
        @SpotBaseUrl baseUrl : String,
        token : CustomTokenProvider
    ) : Retrofit {
        val logging = HttpLoggingInterceptor { message ->
            Log.i("AlbumLogger", message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                token.intercept(chain)
            }
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

}