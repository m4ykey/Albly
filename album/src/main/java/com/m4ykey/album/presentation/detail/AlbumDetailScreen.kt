@file:OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)

package com.m4ykey.album.presentation.detail

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.m4ykey.album.R
import com.m4ykey.album.data.local.model.AlbumEntity
import com.m4ykey.album.domain.model.detail.AlbumRoot
import com.m4ykey.album.mapper.AlbumMapper
import com.m4ykey.album.presentation.components.AlbumButtonRow
import com.m4ykey.album.presentation.components.ErrorCard
import com.m4ykey.album.presentation.components.SaveButtonRow
import com.m4ykey.album.presentation.components.TrackListItem
import com.m4ykey.core.ext.ActionIconButton
import com.m4ykey.core.ext.AppScaffold
import com.m4ykey.core.ext.LoadImage
import com.m4ykey.core.ext.copyText
import org.koin.androidx.compose.koinViewModel

@Composable
fun AlbumDetailScreen(
    id : Int,
    onBack : () -> Unit,
    viewModel : AlbumDetailViewModel = koinViewModel(),
    onTrackClick : (String, String) -> Unit
) {

    val albumDetail by viewModel.detail.collectAsStateWithLifecycle()

    val state = rememberLazyListState()

    LaunchedEffect(viewModel) {
        viewModel.getAlbumById(id)
    }

    val snackbarHostState = remember { SnackbarHostState() }

    AppScaffold(
        snackbarHostState = snackbarHostState,
        navigation = {
            ActionIconButton(
                onClick = onBack,
                textRes = R.string.back,
                iconRes = R.drawable.ic_arrow_left
            )
        },
        content = { padding ->
            AlbumDetailDisplay(
                snackbarHostState = snackbarHostState,
                onTrackClick = onTrackClick,
                state = state,
                albumDetail = albumDetail,
                paddingValues = padding,
                onSaveToggle = { entity ->
                    viewModel.toggleSave(
                        album = entity,
                    )
                },
                onListenLaterToggle = { entity ->
                    viewModel.toggleListenLater(
                        album = entity
                    )
                }
            )
        }
    )
}

@Composable
fun AlbumDetailDisplay(
    modifier: Modifier = Modifier,
    albumDetail : DetailUiState,
    paddingValues: PaddingValues,
    onTrackClick: (String, String) -> Unit,
    state : LazyListState,
    onSaveToggle: (AlbumEntity) -> Unit,
    onListenLaterToggle: (AlbumEntity) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    when (albumDetail) {
        is DetailUiState.Loading -> {
            Box(modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                ContainedLoadingIndicator()
            }
        }
        is DetailUiState.Error -> {
            ErrorCard(text = albumDetail.message)
            Log.i("AlbumDetailError", albumDetail.message)
        }
        is DetailUiState.Success -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                AlbumDetailContent(
                    item = albumDetail.item,
                    contentPadding = paddingValues,
                    state = state,
                    onTrackClick = onTrackClick,
                    onSaveToggle = onSaveToggle,
                    onListenLaterToggle = onListenLaterToggle,
                    isSaved = albumDetail.isSaved,
                    isListenLaterSaved = albumDetail.isListenLaterSaved,
                    snackbarHostState = snackbarHostState
                )
            }
        }
    }
}

@Composable
fun AlbumDetailContent(
    contentPadding : PaddingValues = PaddingValues(0.dp),
    state : LazyListState,
    onTrackClick : (String, String) -> Unit,
    onSaveToggle : (AlbumEntity) -> Unit,
    onListenLaterToggle : (AlbumEntity) -> Unit,
    isSaved : Boolean,
    isListenLaterSaved : Boolean,
    snackbarHostState: SnackbarHostState,
    item : AlbumRoot
) {
    val albumEntity = remember(item) { AlbumMapper.mapToEntity(item) }

    val imageUrl = item.images.find { it.type == "primary" }?.uri ?: item.images.firstOrNull()?.uri
    val textColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    val textColor2 = if (isSystemInDarkTheme()) Color(0xFFBDBDBD) else Color(0xFF424242)

    val albumInfo = "${item.year} • ${item.tracklist.size} " +
            stringResource(R.string.tracks)

    val artists = item.artists.joinToString(", ") { it.name }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    //val trackEntities = tracks.itemSnapshotList.items.map { it.toEntity(item.id) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        state = state
    ) {
        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                LoadImage(
                    imageUrl = imageUrl.orEmpty(),
                    modifier = Modifier.size(260.dp)
                )
            }
        }

        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = item.title,
                    fontSize = 25.sp,
                    color = textColor,
                    modifier = Modifier.clickable {
                        copyText(text = item.title, context)
                    }
                )
                Text(
                    text = artists,
                    fontSize = 15.sp,
                    color = textColor2
                )
            }
        }

        item {
            SaveButtonRow(
                isSaved = isSaved,
                isListenLaterSaved = isListenLaterSaved,
                onSaveClick = {
                    if (albumEntity != null) {
                        onSaveToggle(albumEntity)
                    }
                },
                onListenLaterClick = {
                    if (albumEntity != null) {
                        onListenLaterToggle(albumEntity)
                    }
                }
            )
        }

        item {
            Text(
                text = albumInfo,
                color = textColor2,
                fontSize = 14.sp
            )
        }

        item {
            AlbumButtonRow(
                onAlbumClick = {
                    //context.startActivity(Intent(Intent.ACTION_VIEW, albumUrl.toUri()))
                },
                onArtistClick = {
                    //context.startActivity(Intent(Intent.ACTION_VIEW, artistUrl.toUri()))
                }
            )
        }

        items(
            count = item.tracklist.size,
            key = { index ->
                item.tracklist[index].position
            },
            contentType = { "track_item" }
        ) { index ->
            val currentItem = item.tracklist[index]
            TrackListItem(
                onTrackClick = onTrackClick,
                duration = currentItem.duration,
                title = currentItem.title,
                artists = artists,
                position = currentItem.position
            )
        }
    }
}