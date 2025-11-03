package com.m4ykey.albly.album.presentation.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.m4ykey.albly.album.domain.model.AlbumItem

@Composable
fun AlbumCard(
    modifier: Modifier = Modifier,
    item : AlbumItem
) {
    val largestImageUrl = item.images.maxByOrNull { it.width * it.height }?.url

    val textColor = if (isSystemInDarkTheme()) Color.White else Color.Black

    Column {
        Card(
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            AsyncImage(
                contentDescription = null,
                model = largestImageUrl,
                contentScale = ContentScale.Crop,
                modifier = modifier.fillMaxWidth()
            )
        }
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