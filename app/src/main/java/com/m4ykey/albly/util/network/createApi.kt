package com.m4ykey.albly.util.network

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

inline fun <reified T> createApi(baseUrl : String, moshi : Moshi, client : OkHttpClient) : T {
    require(baseUrl.isNotBlank()) { "Base URL cannot be blank" }

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    return retrofit.create(T::class.java)
}