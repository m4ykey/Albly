package com.m4ykey.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ArtistCard(
    title : String,
    cover : String,
    id : Int,
    onArtistClick : (Int) -> Unit
) {
    val textColor = if (isSystemInDarkTheme()) Color.White else Color.Black

    Row(
        modifier = Modifier
            .clickable { onArtistClick(id) }
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        LoadImage(
            imageUrl = cover,
            modifier = Modifier.size(90.dp)
        )
        Text(
            text = title,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            color = textColor,
            fontSize = 17.sp
        )
    }
}