package com.m4ykey.core.ui

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

@Composable
fun AlbumCard(
    albumName : String,
    artistName : String,
    image : String,
    onAlbumClick : () -> Unit,
    onLongAlbumClick : () -> Unit = {}
) {
    val textColor = if (isSystemInDarkTheme()) Color.White else Color.Black

    Column(
        modifier = Modifier
            .combinedClickable(
                onClick = { onAlbumClick() },
                onLongClick = { onLongAlbumClick() }
            )
    ) {
        LoadImage(imageUrl = image)
        Text(
            text = albumName,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            color = textColor,
            fontSize = 17.sp
        )
        Text(
            text = artistName,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            color = textColor,
            fontSize = 13.sp
        )
    }
}