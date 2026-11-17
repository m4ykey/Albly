package com.m4ykey.core.database.model.search

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "search_item_table",
    indices = [Index(value = ["text"], unique = true)]
)
data class SearchItem(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id : Int = 0,
    @ColumnInfo(name = "text") val text : String
)
