package com.m4ykey.albly.auth.network.di

import com.m4ykey.albly.auth.network.AuthApi
import com.m4ykey.albly.di.AuthBaseUrl
import com.m4ykey.albly.di.AuthRetrofit
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
object AuthModule {

    @Provides
    @Singleton
    @AuthRetrofit
    fun provideAuthRetrofit(
        moshi: Moshi,
        @AuthBaseUrl baseUrl : String
    ) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(@AuthRetrofit retrofit: Retrofit) : AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

}