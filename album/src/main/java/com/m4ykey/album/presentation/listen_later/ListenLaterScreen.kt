@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package com.m4ykey.album.presentation.listen_later

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Button
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.m4ykey.album.R
import com.m4ykey.album.data.local.model.AlbumEntity
import com.m4ykey.album.data.mapper.toAlbum
import com.m4ykey.core.ext.ActionIconButton
import com.m4ykey.core.ext.AppScaffold
import com.m4ykey.core.ui.AlbumGridCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun ListenLaterScreen(
    onBack : () -> Unit,
    onSearchClick : () -> Unit,
    viewModel: ListenLaterViewModel = koinViewModel(),
    onAlbumClick : (String) -> Unit
) {
    val albums by viewModel.albums.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val randomAlbum by viewModel.randomAlbum.collectAsStateWithLifecycle()

    val state = rememberLazyGridState()

    LaunchedEffect(viewModel) {
        viewModel.loadAlbums()
    }

    LaunchedEffect(randomAlbum) {
        randomAlbum?.let { item ->
            onAlbumClick(item.id)
            viewModel.clearAlbum()
        }
    }

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
                onRandomAlbumClick = { viewModel.getRandomAlbum() },
                albums = albums,
                isLoading = isLoading,
                state = state,
                onAlbumClick = onAlbumClick
            )
        }
    )
}

@Composable
fun ListenLaterContent(
    modifier: Modifier = Modifier,
    onRandomAlbumClick : () -> Unit,
    isLoading : Boolean,
    albums : List<AlbumEntity>,
    state : LazyGridState,
    onAlbumClick: (String) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "${stringResource(R.string.album_count)} ${albums.size}",
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
                Icon(
                    contentDescription = null,
                    painter = painterResource(R.drawable.ic_random),
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = stringResource(R.string.random_album))
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .weight(1f)
        ) {
            LazyVerticalGrid(
                state = state,
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(bottom = 10.dp, start = 10.dp, end = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = albums,
                    key = { it.id }
                ) { item ->
                    AlbumGridCard(
                        item = item.toAlbum(),
                        onAlbumClick = { onAlbumClick(item.id) }
                    )
                }
            }

            if (isLoading && albums.isEmpty()) {
                ContainedLoadingIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}