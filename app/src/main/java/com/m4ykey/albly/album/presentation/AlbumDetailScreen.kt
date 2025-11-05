package com.m4ykey.albly.album.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AlbumDetailScreen(
    id : String,
    onBack : () -> Unit
) {
    Text(text = "ID: $id")
}