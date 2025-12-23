@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package com.m4ykey.albly.album.presentation.listen_later

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Button
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.m4ykey.albly.R
import com.m4ykey.albly.album.data.local.model.AlbumEntity
import com.m4ykey.albly.collection.presentation.components.AlbumGridCard
import com.m4ykey.core.ext.ActionIconButton
import com.m4ykey.core.ext.AppScaffold
import org.koin.androidx.compose.koinViewModel

@Composable
fun ListenLaterScreen(
    onBack : () -> Unit,
    onSearchClick : () -> Unit,
    viewModel: ListenLaterViewModel = koinViewModel()
) {
    val albums by viewModel.albums.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    val state = rememberLazyStaggeredGridState()

    LaunchedEffect(viewModel) {
        viewModel.loadAlbums()
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
                onRandomAlbumClick = {},
                albums = albums,
                isLoading = isLoading,
                state = state
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
    state : LazyStaggeredGridState
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .weight(1f)
        ) {
            LazyVerticalStaggeredGrid(
                state = state,
                modifier = Modifier.fillMaxSize(),
                columns = StaggeredGridCells.Fixed(3),
                contentPadding = PaddingValues(bottom = 10.dp, start = 10.dp, end = 10.dp)
            ) {
                items(
                    items = albums,
                    key = { it.id }
                ) { item ->
                    AlbumGridCard(
                        item = item,
                        onAlbumClick = {}
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