package com.m4ykey.albly.track.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m4ykey.albly.album.domain.model.AlbumArtist
import com.m4ykey.albly.album.domain.model.ExternalUrls
import com.m4ykey.albly.track.domain.model.TrackItem
import com.m4ykey.core.ext.formatTrackDuration
import java.util.Locale

@Composable
fun TrackListItem(
    modifier: Modifier = Modifier,
    item : TrackItem,
    onTrackClick : (String, String) -> Unit
) {
    val textColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    val textColor2 = if (isSystemInDarkTheme()) Color(0xFFBDBDBD) else Color(0xFF424242)

    val trackDuration = formatTrackDuration(item.durationMs / 1000)

    val artists = item.artists.joinToString(", ") { it.name }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onTrackClick(item.name, artists)
            }
    ) {
        Row {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.name,
                    color = textColor,
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (item.explicit) Explicit(
                        modifier = Modifier.wrapContentWidth(Alignment.Start)
                    )
                    Text(
                        text = artists,
                        fontSize = 14.sp,
                        overflow = TextOverflow.Ellipsis,
                        color = textColor2,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Text(
                text = trackDuration,
                color = textColor,
                modifier = Modifier
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
    Surface(
        color = Color(0xFF525252),
        shape = RoundedCornerShape(3.dp),
        contentColor = Color.White
    ) {
        Text(
            text = "E",
            color = Color.White,
            fontSize = 10.sp,
            modifier = modifier.padding(horizontal = 5.dp, vertical = 1.dp)
        )
    }
}

@Preview
@Composable
private fun ExplicitPrev() {
    Explicit()
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
            name = $$"A$AP Rocky"
        )
    )
    val trackItems = (1..15).map { index ->
        TrackItem(
            id = "id_$index",
            name = "Bottoms Up $index",
            previewUrl = "",
            trackNumber = index,
            durationMs = 402 * index,
            discNumber = 2,
            explicit = index % 2 == 0,
            externalUrls = externalUrls,
            artists = artists
        )
    }
    LazyColumn {
        items(trackItems) { item ->
            TrackListItem(
                item = item,
                onTrackClick = { item1, item2 ->

                }
            )
        }
    }
}