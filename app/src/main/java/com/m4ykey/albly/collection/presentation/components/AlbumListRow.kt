package com.m4ykey.albly.collection.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.m4ykey.albly.album.data.local.model.AlbumEntity
import com.m4ykey.albly.album.data.local.model.ArtistEntity
import com.m4ykey.albly.album.data.local.model.ExternalUrlsEntity
import com.m4ykey.core.ext.LoadImage

@Composable
fun AlbumListRow(
    item : AlbumEntity,
    onAlbumClick : (String) -> Unit
) {
    val largestImageUrl = item.images.maxByOrNull { it.width * it.height }?.url
    val artists = item.artists.joinToString(", ") { it.name }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(bottom = 10.dp)
            .clickable { onAlbumClick(item.id) }
    ) {
        LoadImage(
            imageUrl = largestImageUrl.orEmpty(),
            size = 90.dp
        )
        Column(
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item.name,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = artists,
                modifier = Modifier.fillMaxWidth(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
        }
    }
}

@Preview
@Composable
private fun AlbumListRowPrev() {
    val externalUrls = ExternalUrlsEntity(
        spotify = ""
    )
    val artists = listOf(
        ArtistEntity(
            name = "Artist1",
            id = "",
            externalUrls = externalUrls
        ),
        ArtistEntity(
            name = "Artist2",
            id = "",
            externalUrls = externalUrls
        )
    )
    val item = AlbumEntity(
        id = "",
        label = "",
        images = emptyList(),
        albumType = "",
        copyrights = emptyList(),
        name = "Test Album 123",
        releaseDate = "",
        totalTracks = 0,
        externalUrls = externalUrls,
        artists = artists
    )
    AlbumListRow(
        item = item,
        onAlbumClick = {}
    )
}