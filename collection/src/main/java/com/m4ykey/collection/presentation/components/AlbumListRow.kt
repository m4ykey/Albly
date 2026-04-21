package com.m4ykey.collection.presentation.components

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
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m4ykey.album.data.local.model.AlbumEntity
import com.m4ykey.core.ext.LoadImage

@Composable
fun AlbumListRow(
    item : AlbumEntity,
    onAlbumClick : (Int) -> Unit
) {
    val artists = item.artistList.joinToString(", ") { it.name }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(bottom = 10.dp)
            .clickable { onAlbumClick(item.id) }
    ) {
        LoadImage(
            imageUrl = item.image,
            modifier = Modifier.size(90.dp)
        )
        Column(
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item.title,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 17.sp
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = artists,
                modifier = Modifier.fillMaxWidth(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                fontSize = 13.sp
            )
        }
    }
}