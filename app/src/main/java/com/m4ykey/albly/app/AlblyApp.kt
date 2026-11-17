package com.m4ykey.albly.app

import android.app.Application
import com.m4ykey.albly.app.di.appModule
import com.m4ykey.album.di.albumModule
import com.m4ykey.artist.di.artistModule
import com.m4ykey.collection.di.collectionViewModel
import com.m4ykey.core.database.databaseModule
import com.m4ykey.lyrics.di.lyricsModule
import com.m4ykey.network.BuildConfig
import com.m4ykey.network.networkModule
import com.m4ykey.search.di.searchModule
import com.m4ykey.settings.di.settingsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

val modules = listOf(
    lyricsModule,
    searchModule,
    databaseModule,
    albumModule,
    collectionViewModel,
    networkModule,
    settingsModule,
    appModule,
    artistModule
)

class AlblyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@AlblyApp)
            modules(modules)
        }
    }
}