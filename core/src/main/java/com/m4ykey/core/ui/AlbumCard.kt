package com.m4ykey.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.m4ykey.core.ext.LoadImage

@Composable
fun AlbumCard(
    item : AlbumItem,
    onAlbumClick : (String) -> Unit
) {
    val largestImageUrl = item.images.maxByOrNull { it.width * it.height }?.url

    val textColor = if (isSystemInDarkTheme()) Color.White else Color.Black

    Column(
        modifier = Modifier.clickable { onAlbumClick(item.id) }
    ) {
        largestImageUrl?.let { LoadImage(imageUrl = it) }
        Text(
            text = item.name,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            color = textColor,
            fontSize = 17.sp
        )
        Text(
            text = item.artists.joinToString(", ") { it.name },
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            color = textColor,
            fontSize = 13.sp
        )
    }
}