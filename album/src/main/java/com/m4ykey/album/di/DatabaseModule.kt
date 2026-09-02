package com.m4ykey.album.di

import androidx.room.Room
import com.m4ykey.album.data.database.AppDatabase
import com.m4ykey.album.data.local.migration.MIGRATION_1_2
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "app_database"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    single { get<AppDatabase>().albumDao() }
}