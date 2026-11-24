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

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL("ALTER TABLE lyrics_table ADD COLUMN image TEXT NOT NULL DEFAULT ''")
    }
}

val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS is_lyrics_saved_table")
        connection.execSQL("""
            CREATE TABLE IF NOT EXISTS is_lyrics_saved_table (
                isLyricsSaved INTEGER NOT NULL,
                remote_id INTEGER NOT NULL DEFAULT 0,
                isrc TEXT NOT NULL DEFAULT '',
                source TEXT NOT NULL,
                PRIMARY KEY(source, remote_id, isrc)
            )
        """.trimIndent())

        connection.execSQL("""
            CREATE TABLE IF NOT EXISTS lyrics_table_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                source TEXT NOT NULL,
                remote_id INTEGER NOT NULL,
                isrc TEXT NOT NULL DEFAULT '',
                title TEXT NOT NULL,
                artist TEXT NOT NULL,
                album TEXT NOT NULL,
                lyrics TEXT NOT NULL,
                save_time INTEGER,
                image TEXT NOT NULL DEFAULT ''
            )
        """.trimIndent())

        connection.execSQL("""
            INSERT INTO lyrics_table_new (id, source, remote_id, isrc, title, artist, album, lyrics, save_time, image)
            SELECT id, source, remote_id, COALESCE(isrc, ''), title, artist, album, lyrics, save_time, image
            FROM lyrics_table
        """.trimIndent())

        connection.execSQL("DROP TABLE lyrics_table")
        connection.execSQL("ALTER TABLE lyrics_table_new RENAME TO lyrics_table")
    }
}

val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL("""
            CREATE TABLE IF NOT EXISTS search_item_table (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                text TEXT NOT NULL
            )
        """.trimIndent())

        connection.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_search_item_table_text ON search_item_table (text)")
    }
}

val MIGRATION_7_8 = object : Migration(7, 8) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL("""
            CREATE TABLE IF NOT EXISTS new_release_table (
                masterId INTEGER NOT NULL,
                title TEXT NOT NULL,
                thumb TEXT NOT NULL,
                coverImage TEXT NOT NULL,
                artist TEXT NOT NULL,
                position INTEGER NOT NULL,
                fetchedAt INTEGER NOT NULL,
                year INTEGER NOT NULL,
                PRIMARY KEY(masterId)
            )
        """.trimIndent())

        connection.execSQL("""
            CREATE TABLE IF NOT EXISTS new_release_cursor (
                id INTEGER NOT NULL,
                fetchedAt INTEGER NOT NULL,
                nextPage INTEGER,
                PRIMARY KEY(id)
            )
        """.trimIndent())
    }
}

val MIGRATION_8_9 = object : Migration(8, 9) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL("""
            CREATE TABLE IF NOT EXISTS artist_release_entity (
                artistId INTEGER NOT NULL,
                releaseType TEXT NOT NULL,
                releaseId INTEGER NOT NULL,
                title TEXT NOT NULL,
                thumb TEXT NOT NULL,
                year INTEGER NOT NULL,
                resourceUrl TEXT NOT NULL,
                position INTEGER NOT NULL,
                fetchedAt INTEGER NOT NULL,
                PRIMARY KEY(artistId, releaseType, releaseId)
            )
        """.trimIndent())

        connection.execSQL("""
            CREATE TABLE IF NOT EXISTS artist_release_cursor (
                artistId INTEGER NOT NULL,
                fetchedAt INTEGER NOT NULL,
                nextPage INTEGER,
                PRIMARY KEY(artistId)
            )
        """.trimIndent())
    }
}

val MIGRATION_9_10 = object : Migration(9, 10) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL("""
            CREATE TABLE IF NOT EXISTS is_artist_saved_table (
                id INTEGER NOT NULL,
                isArtistSaved INTEGER NOT NULL,
                PRIMARY KEY(id)
            )
        """.trimIndent())

        connection.execSQL("""
            CREATE TABLE IF NOT EXISTS artist_table (
                id INTEGER NOT NULL,
                name TEXT NOT NULL,
                uri TEXT NOT NULL,
                images TEXT NOT NULL,
                realname TEXT NOT NULL,
                profile TEXT NOT NULL,
                urls TEXT NOT NULL,
                save_time INTEGER,
                PRIMARY KEY(id)
            )
        """.trimIndent())
    }
}