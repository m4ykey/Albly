@file:OptIn(ExperimentalMaterial3Api::class)

package com.m4ykey.albly.album.presentation.listen_later

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.m4ykey.albly.R
import com.m4ykey.core.ext.ActionIconButton
import com.m4ykey.core.ext.AppScaffold

@Composable
fun ListenLaterScreen(
    onBack : () -> Unit,
    onSearchClick : () -> Unit
) {
    AppScaffold(
        navigation = {
            ActionIconButton(
                onClick = onBack,
                iconRes = R.drawable.ic_arrow_left,
                textRes = R.string.back
            )
        },
        actions = {
            ActionIconButton(
                onClick = onSearchClick,
                textRes = R.string.empty,
                iconRes = R.drawable.ic_add
            )
        },
        content = { padding ->
            ListenLaterContent(
                modifier = Modifier.padding(padding),
                onRandomAlbumClick = {}
            )
        }
    )
}

@Composable
fun ListenLaterContent(
    modifier: Modifier = Modifier,
    onRandomAlbumClick : () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "${stringResource(R.string.album_count)}: ",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
        ) {
            Button(
                onClick = { onRandomAlbumClick() },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(R.string.random_album))
            }
        }
    }
}