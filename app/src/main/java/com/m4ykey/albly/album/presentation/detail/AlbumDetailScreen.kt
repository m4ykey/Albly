@file:OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)

package com.m4ykey.albly.album.presentation.detail

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.m4ykey.albly.R
import com.m4ykey.albly.album.data.local.model.AlbumEntity
import com.m4ykey.albly.album.domain.model.AlbumDetail
import com.m4ykey.albly.core.mapper.toEntity
import com.m4ykey.albly.track.data.local.model.TrackEntity
import com.m4ykey.albly.track.domain.model.TrackItem
import com.m4ykey.albly.track.presentation.TrackListItem
import com.m4ykey.albly.track.presentation.TrackViewModel
import com.m4ykey.core.ext.ActionIconButton
import com.m4ykey.core.ext.AppScaffold
import com.m4ykey.core.ext.LoadImage
import com.m4ykey.core.ext.copyText
import com.m4ykey.core.ext.formatReleaseDate
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun AlbumDetailScreen(
    id : String,
    onBack : () -> Unit,
    viewModel : AlbumDetailViewModel = koinViewModel(),
    onTrackClick : (String, String) -> Unit,
    trackViewModel : TrackViewModel = koinViewModel()
) {

    val albumDetail by viewModel.detail.collectAsStateWithLifecycle()
    val tracks = trackViewModel.trackResults.collectAsLazyPagingItems()

    val state = rememberLazyListState()

    LaunchedEffect(viewModel) {
        viewModel.getAlbumById(id)
        trackViewModel.setAlbum(id)
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
                tracks = tracks,
                albumDetail = albumDetail,
                paddingValues = padding,
                onSaveToggle = { entity, track ->
                    viewModel.toggleSave(
                        album = entity,
                        tracks = track
                    )
                },
                onListenLaterToggle = { entity, track ->
                    viewModel.toggleListenLater(
                        album = entity,
                        tracks = track
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
    tracks: LazyPagingItems<TrackItem>,
    onSaveToggle: (AlbumEntity, List<TrackEntity>) -> Unit,
    onListenLaterToggle: (AlbumEntity, List<TrackEntity>) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    when {
        albumDetail.loading -> Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            ContainedLoadingIndicator()
        }
        albumDetail.error != null -> {}
        albumDetail.item != null -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                AlbumDetailContent(
                    item = albumDetail.item,
                    contentPadding = paddingValues,
                    state = state,
                    tracks = tracks,
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
    item : AlbumDetail,
    state : LazyListState,
    tracks : LazyPagingItems<TrackItem>,
    onTrackClick : (String, String) -> Unit,
    onSaveToggle : (AlbumEntity, List<TrackEntity>) -> Unit,
    onListenLaterToggle : (AlbumEntity, List<TrackEntity>) -> Unit,
    isSaved : Boolean,
    isListenLaterSaved : Boolean,
    snackbarHostState: SnackbarHostState
) {
    val albumEntity = remember(item) { item.toEntity() }

    val isAllTracksLoaded = tracks.itemCount >= item.totalTracks

    val largestImageUrl = item.images.maxByOrNull { it.width * it.height }?.url
    val textColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    val textColor2 = if (isSystemInDarkTheme()) Color(0xFFBDBDBD) else Color(0xFF424242)

    val albumType = item.type.replaceFirstChar { it.uppercase() }

    val albumInfo = "$albumType • ${formatReleaseDate(item.releaseDate)} • ${item.totalTracks} " +
            stringResource(R.string.tracks)

    val albumUrl = item.externalUrls.spotify
    val artistUrl = item.artists[0].externalUrls.spotify

    val copyright = item.copyright
        .map { it.text }
        .distinct()
        .joinToString(separator = "\n")

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val trackEntities = tracks.itemSnapshotList.items.map { it.toEntity(item.id) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        state = state,
    ) {
        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                largestImageUrl?.let { LoadImage(
                    imageUrl = it,
                    modifier = Modifier.size(260.dp)
                ) }
            }
        }

        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = item.name,
                    fontSize = 25.sp,
                    color = textColor,
                    modifier = Modifier.clickable {
                        copyText(text = item.name, context)
                    }
                )
                Text(
                    text = item.artists.joinToString(", ") { it.name },
                    fontSize = 15.sp,
                    color = textColor2
                )
            }
        }

        item {
            SaveButtonsRow(
                isSaved = isSaved,
                isListenLaterSaved = isListenLaterSaved,
                onSaveClick = {
                    if (isAllTracksLoaded) {
                        onSaveToggle(albumEntity, trackEntities)
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = context.getString(R.string.please_wait_until_all_tracks_are_loaded)
                            )
                        }
                    }
                },
                onListenLaterClick = {
                    if (isAllTracksLoaded) {
                        onListenLaterToggle(albumEntity, trackEntities)
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = context.getString(R.string.please_wait_until_all_tracks_are_loaded)
                            )
                        }
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
            AlbumButtonsRow(
                onAlbumClick = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, albumUrl.toUri()))
                },
                onArtistClick = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, artistUrl.toUri()))
                }
            )
        }

        items(
            count = tracks.itemCount,
            key = tracks.itemKey { item -> item.id },
            contentType = { "track_item" }
        ) { index ->
            val currentItem = tracks[index]
            currentItem?.let { item ->
                TrackListItem(
                    item = item,
                    onTrackClick = onTrackClick
                )
            }
        }

        item {
            Text(
                text = copyright,
                color = textColor
            )
        }
    }
}

@Composable
fun AlbumButtonsRow(
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

@Composable
fun SaveButtonsRow(
    modifier: Modifier = Modifier,
    isSaved : Boolean,
    isListenLaterSaved : Boolean,
    onSaveClick : () -> Unit,
    onListenLaterClick : () -> Unit
) {
    Row(
        modifier = modifier.wrapContentWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            contentDescription = stringResource(R.string.save),
            painter = if (isSaved) {
                painterResource(R.drawable.ic_favorite)
            } else {
                painterResource(R.drawable.ic_favorite_border)
            },
            modifier = modifier.clickable { onSaveClick() }
        )

        Icon(
            contentDescription = stringResource(R.string.listen_later),
            painter = if (isListenLaterSaved) {
                painterResource(R.drawable.ic_access_time)
            } else {
                painterResource(R.drawable.ic_access_time_outline)
            },
            modifier = modifier.clickable { onListenLaterClick() }
        )
    }
}