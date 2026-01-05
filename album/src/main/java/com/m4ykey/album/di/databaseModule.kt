package com.m4ykey.album.di

import androidx.room.Room
import com.m4ykey.album.data.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    single { get<AppDatabase>().albumDao() }
    single { get<AppDatabase>().trackDao() }
}