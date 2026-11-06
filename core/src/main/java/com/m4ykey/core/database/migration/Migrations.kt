package com.m4ykey.core.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL("ALTER TABLE album_table ADD COLUMN uri TEXT NOT NULL DEFAULT ''")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL("""
            CREATE TABLE IF NOT EXISTS lyrics_table (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                track_key TEXT NOT NULL,
                source TEXT NOT NULL,
                remote_id INTEGER NOT NULL,
                isrc TEXT,
                title TEXT NOT NULL,
                artist TEXT NOT NULL,
                album TEXT NOT NULL,
                lyrics TEXT NOT NULL,
                save_time INTEGER
            )
        """.trimIndent())

        connection.execSQL("""
            CREATE TABLE IF NOT EXISTS is_lyrics_saved_table (
                track_key TEXT NOT NULL PRIMARY KEY,
                isLyricsSaved INTEGER NOT NULL
            )
        """.trimIndent())
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL("""
            CREATE TABLE IF NOT EXISTS lyrics_table_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                source TEXT NOT NULL,
                remote_id INTEGER NOT NULL,
                isrc TEXT,
                title TEXT NOT NULL,
                artist TEXT NOT NULL,
                album TEXT NOT NULL,
                lyrics TEXT NOT NULL,
                save_time INTEGER
            )
        """.trimIndent())

        connection.execSQL("""
            INSERT INTO lyrics_table_new (id, source, remote_id, isrc, title, artist, album, lyrics, save_time)
            SELECT id, source, remote_id, isrc, title, artist, album, lyrics, save_time
            FROM lyrics_table
        """.trimIndent())

        connection.execSQL("DROP TABLE lyrics_table")
        connection.execSQL("ALTER TABLE lyrics_table_new RENAME TO lyrics_table")

        connection.execSQL("DROP TABLE IF EXISTS is_lyrics_saved_table")
        connection.execSQL("""
            CREATE TABLE IF NOT EXISTS is_lyrics_saved_table (
                isLyricsSaved INTEGER NOT NULL,
                remote_id INTEGER NOT NULL DEFAULT 0,
                isrc TEXT,
                source TEXT NOT NULL,
                PRIMARY KEY(source, remote_id)
            )
        """.trimIndent())
    }
}