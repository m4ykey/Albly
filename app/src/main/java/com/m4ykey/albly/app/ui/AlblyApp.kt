package com.m4ykey.albly.app.ui

import android.app.Application
import com.m4ykey.album.di.albumModule
import com.m4ykey.album.di.databaseModule
import com.m4ykey.collection.di.collectionViewModel
import com.m4ykey.lyrics.di.lyricsModule
import com.m4ykey.network.networkModule
import com.m4ykey.search.di.searchModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

val modules = listOf(
    lyricsModule,
    searchModule,
    databaseModule,
    albumModule,
    collectionViewModel,
    networkModule
)

class AlblyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@AlblyApp)
            modules(modules)
        }
    }
}