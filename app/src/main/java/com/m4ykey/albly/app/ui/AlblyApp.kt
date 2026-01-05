package com.m4ykey.albly.app.ui

import android.app.Application
import com.m4ykey.album.di.albumModule
import com.m4ykey.album.di.databaseModule
import com.m4ykey.auth.di.authModule
import com.m4ykey.collection.di.collectionViewModel
import com.m4ykey.lyrics.di.lyricsModule
import com.m4ykey.search.di.searchModule
import com.m4ykey.track.di.trackModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

val modules = listOf(
    authModule,
    lyricsModule,
    trackModule,
    searchModule,
    databaseModule,
    albumModule,
    collectionViewModel
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