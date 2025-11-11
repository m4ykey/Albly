package com.m4ykey.albly.album.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m4ykey.albly.album.domain.model.AlbumArtist
import com.m4ykey.albly.album.domain.model.ExternalUrls
import com.m4ykey.albly.album.domain.model.TrackItem
import java.util.Locale

fun formatTrackDuration(seconds : Int) : String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format(Locale.getDefault(), "%d:%02d", minutes, remainingSeconds)
}

@Composable
fun TrackListItem(
    modifier: Modifier = Modifier,
    item : TrackItem
) {
    val textColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    val textColor2 = if (isSystemInDarkTheme()) Color(0xFFBDBDBD) else Color(0xFF424242)

    val trackDuration = formatTrackDuration(item.durationMs / 1000)

    Column(modifier = modifier.fillMaxWidth()) {
        Row {
            Column(
                modifier = modifier.weight(1f)
            ) {
                Text(
                    text = item.name,
                    color = textColor,
                    fontSize = 16.sp,
                    modifier = modifier.fillMaxWidth()
                )
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (item.explicit) Explicit()
                    Text(
                        text = item.artists.joinToString(", ") { it.name },
                        fontSize = 14.sp,
                        overflow = TextOverflow.Ellipsis,
                        color = textColor2,
                        maxLines = 1,
                        modifier = modifier.fillMaxWidth()
                    )
                }
            }
            Text(
                text = trackDuration,
                color = textColor,
                modifier = modifier
                    .align(Alignment.CenterVertically)
                    .padding(10.dp)
            )
        }
    }
}

@Composable
fun Explicit(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(3.dp))
            .background(Color(0xFF525252))
            .padding(horizontal = 5.dp, vertical = 1.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "E",
            color = Color.White,
            fontSize = 10.sp
        )
    }
}

@Preview
@Composable
private fun TrackListItemPrev() {
    val externalUrls = ExternalUrls("")
    val artists = listOf(
        AlbumArtist(
            externalUrls = externalUrls,
            id = "",
            type = "",
            name = "Trey Songz"
        ),
        AlbumArtist(
            externalUrls = externalUrls,
            id = "",
            type = "",
            name = "Nicki Minaj"
        ),
    )
    val item = TrackItem(
        id = "",
        name = "Bottoms Up",
        previewUrl = "",
        trackNumber = 1,
        durationMs = 402,
        discNumber = 1,
        explicit = false,
        externalUrls = externalUrls,
        artists = artists
    )
    TrackListItem(item = item)
}