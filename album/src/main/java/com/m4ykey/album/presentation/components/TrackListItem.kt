package com.m4ykey.album.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TrackListItem(
    modifier: Modifier = Modifier,
    title : String,
    artists : String,
    duration : String,
    onTrackClick : (String, String, String) -> Unit,
    position : String,
    img : String
) {
    val textColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    val textColor2 = if (isSystemInDarkTheme()) Color(0xFFBDBDBD) else Color(0xFF424242)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onTrackClick(title, artists, img) }
            .padding(vertical = 8.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = position,
            color = textColor,
            modifier = Modifier.width(30.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = title,
                color = textColor,
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = artists,
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis,
                color = textColor2,
                maxLines = 1
            )
        }
        if (duration.isNotEmpty()) {
            Text(
                fontSize = 13.sp,
                text = duration,
                color = textColor,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}