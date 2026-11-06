package com.m4ykey.lyrics.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.m4ykey.core.ui.LoadImage

@Composable
fun LyricsList(
    modifier: Modifier = Modifier,
    title : String,
    artist : String,
    image : String,
    onClick : () -> Unit
) {
    Row(modifier = modifier.fillMaxWidth()) {
        LoadImage(
            imageUrl = image
        )
        Column() {
            Text(text = title)
            Text(text = artist)
        }
    }
}