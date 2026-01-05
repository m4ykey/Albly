package com.m4ykey.album.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.m4ykey.album.R

@Composable
fun AlbumButtonRow(
    modifier: Modifier = Modifier,
    onArtistClick : () -> Unit,
    onAlbumClick : () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Button(
            onClick = { onArtistClick() },
            modifier = modifier.weight(1f)
        ) {
            Icon(
                contentDescription = null,
                painter = painterResource(R.drawable.ic_artist)
            )
            Spacer(modifier.width(5.dp))
            Text(text = stringResource(R.string.artist))
        }
        Button(
            onClick = { onAlbumClick() },
            modifier = modifier.weight(1f)
        ) {
            Icon(
                contentDescription = null,
                painter = painterResource(R.drawable.ic_album)
            )
            Spacer(modifier.width(5.dp))
            Text(text = stringResource(R.string.album))
        }
    }
}