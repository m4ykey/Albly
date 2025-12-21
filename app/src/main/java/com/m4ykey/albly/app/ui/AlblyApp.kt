package com.m4ykey.albly.app.ui

import android.app.Application
import com.m4ykey.albly.di.databaseModule
import com.m4ykey.albly.di.repositoryModule
import com.m4ykey.albly.di.serviceModule
import com.m4ykey.albly.di.useCaseModule
import com.m4ykey.albly.di.viewModelModule
import com.m4ykey.auth.di.authModule
import com.m4ykey.auth.di.dataStoreModule
import com.m4ykey.auth.di.scopeModule
import com.m4ykey.auth.di.spotifyApiModule
import com.m4ykey.lyrics.di.lyricsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

val modules = listOf(
    repositoryModule,
    viewModelModule,
    serviceModule,
    useCaseModule,
    authModule,
    spotifyApiModule,
    dataStoreModule,
    scopeModule,
    databaseModule
) + lyricsModule

class AlblyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AlblyApp)
            modules(modules)
        }
    }
}