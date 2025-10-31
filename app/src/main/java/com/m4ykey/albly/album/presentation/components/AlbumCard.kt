package com.m4ykey.albly.album.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.SubcomposeAsyncImage
import com.m4ykey.albly.album.domain.model.AlbumItem

@Composable
fun AlbumCard(
    modifier: Modifier = Modifier,
    item : AlbumItem
) {
    val largestImageUrl = remember(item.images) {
        item.images.maxByOrNull { it.width * it.height }?.url
    }

    Column {
        SubcomposeAsyncImage(
            contentDescription = null,
            model = largestImageUrl,
            contentScale = ContentScale.Crop
        )
        Text(text = item.name)
    }
}