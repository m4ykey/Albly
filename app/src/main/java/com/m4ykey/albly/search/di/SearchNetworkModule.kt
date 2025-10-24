package com.m4ykey.albly.search.di

import com.m4ykey.albly.di.SearchRetrofit
import com.m4ykey.albly.di.SpotBaseUrl
import com.m4ykey.albly.search.data.network.api.SearchApi
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
        @SpotBaseUrl baseUrl: String
    ) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideSearchApi(@SearchRetrofit retrofit : Retrofit) : SearchApi {
        return retrofit.create(SearchApi::class.java)
    }

}