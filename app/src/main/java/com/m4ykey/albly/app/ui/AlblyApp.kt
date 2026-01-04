package com.m4ykey.albly.app.ui

import android.app.Application
import com.m4ykey.albly.di.albumRepositoryModule
import com.m4ykey.albly.di.albumServiceModule
import com.m4ykey.albly.di.albumUseCaseModule
import com.m4ykey.albly.di.albumViewModelModule
import com.m4ykey.albly.di.databaseModule
import com.m4ykey.albly.di.searchRepositoryModule
import com.m4ykey.albly.di.searchUseCaseModule
import com.m4ykey.albly.di.searchServiceModule
import com.m4ykey.albly.di.searchViewModelModule
import com.m4ykey.auth.di.authModule
import com.m4ykey.lyrics.di.lyricsModule
import com.m4ykey.track.di.trackModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

val modules = listOf(
    searchViewModelModule,
    searchServiceModule,
    albumViewModelModule,
    albumServiceModule,
    databaseModule,
    albumUseCaseModule,
    searchUseCaseModule,
    searchRepositoryModule,
    albumRepositoryModule,
    authModule,
    lyricsModule,
    trackModule
)

class AlblyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AlblyApp)
            modules(modules)
        }
    }
}