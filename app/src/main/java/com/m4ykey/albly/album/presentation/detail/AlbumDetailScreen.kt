package com.m4ykey.albly.album.presentation.detail

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.m4ykey.albly.R
import com.m4ykey.albly.album.domain.model.AlbumDetail
import com.m4ykey.albly.album.domain.model.TrackItem
import com.m4ykey.albly.album.presentation.components.TrackListItem
import com.m4ykey.albly.util.formatReleaseDate
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AlbumDetailScreen(
    id : String,
    onBack : () -> Unit,
    viewModel : AlbumDetailViewModel = koinViewModel()
) {

    val albumDetail by viewModel.detail.collectAsStateWithLifecycle()
    val tracks = viewModel.trackResults.collectAsLazyPagingItems()

    val state = rememberLazyListState()

    LaunchedEffect(viewModel) {
        viewModel.getAlbumById(id)
        viewModel.setAlbum(id)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            contentDescription = stringResource(id = R.string.back),
                            imageVector = Icons.AutoMirrored.Default.ArrowBack
                        )
                    }
                }
            )
        }
    ) { padding ->
        when {
            albumDetail.loading -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                ContainedLoadingIndicator()
            }
            albumDetail.error != null -> {}
            albumDetail.item != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    AlbumDetailContent(
                        item = albumDetail.item!!,
                        contentPadding = padding,
                        state = state,
                        tracks = tracks
                    )
                }
            }
        }
    }
}

@Composable
fun AlbumDetailContent(
    contentPadding : PaddingValues = PaddingValues(0.dp),
    item : AlbumDetail,
    state : LazyListState,
    tracks : LazyPagingItems<TrackItem>
) {
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
                Card(
                    shape = RoundedCornerShape(10.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.size(260.dp)
                ) {
                    CoilImage(
                        imageModel = { largestImageUrl },
                        imageOptions = ImageOptions(
                            alignment = Alignment.Center,
                            contentScale = ContentScale.Crop
                        )
                    )
                }
            }
        }

        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = item.name,
                    fontSize = 25.sp,
                    color = textColor
                )
                Text(
                    text = item.artists.joinToString(", ") { it.name },
                    fontSize = 15.sp,
                    color = textColor2
                )
            }
        }

        item { SaveButtonsRow() }

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
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(albumUrl)))
                },
                onArtistClick = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(artistUrl)))
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
                    item = item
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
    modifier: Modifier = Modifier
) {
    var isSaveClicked by remember { mutableStateOf(false) }
    var isListenLaterClicked by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.wrapContentWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            contentDescription = stringResource(R.string.save),
            imageVector = if (isSaveClicked) {
                Icons.Outlined.Favorite
            } else {
                Icons.Outlined.FavoriteBorder
            },
            modifier = modifier.clickable { isSaveClicked = !isSaveClicked }
        )

        Icon(
            contentDescription = stringResource(R.string.listen_later),
            imageVector = if (isListenLaterClicked) {
                Icons.Default.AccessTimeFilled
            } else {
                Icons.Outlined.AccessTime
            },
            modifier = modifier.clickable { isListenLaterClicked = !isListenLaterClicked }
        )
    }
}