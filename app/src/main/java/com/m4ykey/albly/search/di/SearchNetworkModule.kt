package com.m4ykey.albly.search.di

import android.util.Log
import com.m4ykey.albly.auth.token.CustomTokenProvider
import com.m4ykey.albly.di.SearchRetrofit
import com.m4ykey.albly.di.SpotBaseUrl
import com.m4ykey.albly.search.data.network.SearchService
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
object SearchNetworkModule {

    @Provides
    @Singleton
    @SearchRetrofit
    fun provideSearchRetrofit(
        moshi: Moshi,
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

    @Provides
    @Singleton
    fun provideSearchService(@SearchRetrofit retrofit : Retrofit) : SearchService {
        return retrofit.create(SearchService::class.java)
    }

}