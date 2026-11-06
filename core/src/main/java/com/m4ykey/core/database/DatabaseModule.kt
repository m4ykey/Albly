package com.m4ykey.core.database

import androidx.room.Room
import com.m4ykey.core.database.migration.MIGRATION_1_2
import com.m4ykey.core.database.migration.MIGRATION_2_3
import com.m4ykey.core.database.migration.MIGRATION_3_4
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "app_database"
        )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
            .build()
    }

    single { get<AppDatabase>().albumDao() }
    single { get<AppDatabase>().lyricsDao() }
}