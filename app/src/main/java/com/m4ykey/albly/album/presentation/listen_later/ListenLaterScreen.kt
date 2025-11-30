@file:OptIn(ExperimentalMaterial3Api::class)

package com.m4ykey.albly.album.presentation.listen_later

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.m4ykey.albly.R

@Composable
fun ListenLaterScreen(
    modifier: Modifier = Modifier,
    onBack : () -> Unit,
    onSearchClick : () -> Unit
) {

    var showSearch by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {  },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            contentDescription = stringResource(id = R.string.back),
                            imageVector = Icons.AutoMirrored.Default.ArrowBack
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            contentDescription = null,
                            imageVector = Icons.Default.Add
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding)
        ) {
            Text(
                text = "${stringResource(R.string.album_count)}: ",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {},
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = stringResource(R.string.random_album))
                }
            }
        }
    }
}