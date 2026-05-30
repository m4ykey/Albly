package com.m4ykey.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.m4ykey.core.ext.LoadImage

@Composable
fun LyricsCard(
    modifier: Modifier = Modifier,
    onClick : () -> Unit,
    imageUrl : String,
    title : String,
    artist : String
) {

    val textColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    val textColor2 = if (isSystemInDarkTheme()) Color(0xFFBDBDBD) else Color(0xFF424242)

    Row(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        LoadImage(
            imageUrl = imageUrl,
            modifier = Modifier.size(90.dp)
        )
        Column {
            Text(
                text = title,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                color = textColor,
                fontSize = 17.sp
            )
            Text(
                text = artist,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                color = textColor2,
                fontSize = 14.sp
            )
        }
    }
}