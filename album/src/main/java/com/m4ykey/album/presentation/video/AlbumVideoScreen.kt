package com.m4ykey.album.presentation.video

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.m4ykey.album.R
import com.m4ykey.album.domain.model.detail.Video
import com.m4ykey.core.ui.ActionIconButton
import com.m4ykey.core.ui.AppScaffold
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

private val videoShape = RoundedCornerShape(16.dp)

private val youtubeIdPattern = Regex(
    """(?:youtu\.be/|/embed/|/v/|/shorts/|/live/|[?&]v=)([A-Za-z0-9_-]{11})"""
)

private val rawYoutubeIdPattern = Regex("""^[A-Za-z0-9_-]{11}$""")

@Composable
fun AlbumVideoScreen(
    modifier: Modifier = Modifier,
    onBack : () -> Unit,
    videos : List<Video>
) {
    val entries = remember(videos) {
        videos.mapNotNull { video ->
            val id = extractId(video.uri)
            if (id.isEmpty()) null else VideoEntry(id = id, name = video.title.trim())
        }.distinctBy { it.id }
    }

    var activeIndex by rememberSaveable { mutableIntStateOf(-1) }
    val activeId = entries.getOrNull(activeIndex)?.id ?: entries.firstOrNull()?.id
    val videosLabel = stringResource(R.string.videos)

    AppScaffold(
        content = { padding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                if (activeId != null) {
                    Card(
                        shape = videoShape,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    ) {
                        VideoPlayer(
                            videoId = activeId,
                            playToken = activeIndex,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(
                        items = entries,
                        key = { _, entry -> entry.id }
                    ) { index, entry ->
                        VideoRow(
                            title = entry.name.ifBlank { "$videosLabel ${index + 1}" },
                            isActive = index == activeIndex,
                            onClick = { activeIndex = index },
                        )
                    }
                }
            }
        },
        navigation = {
            ActionIconButton(
                onClick = onBack,
                iconRes = R.drawable.ic_arrow_left,
                textRes = R.string.back
            )
        }
    )
}

@Composable
private fun VideoRow(
    isActive : Boolean,
    onClick : () -> Unit,
    modifier: Modifier = Modifier,
    title : String
) {
    val containerColor = if (isActive) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceContainer
    }

    val contentColor = if (isActive) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(
        shape = videoShape,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = contentColor.copy(alpha = 0.12f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = contentColor
                )
            }

            Text(
                text = title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                color = contentColor
            )
        }
    }
}

@Composable
private fun VideoPlayer(
    videoId : String,
    playToken : Int,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var youTubePlayer by remember { mutableStateOf<YouTubePlayer?>(null) }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            YouTubePlayerView(context).apply {
                lifecycleOwner.lifecycle.addObserver(this)

                addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(player: YouTubePlayer) {
                        youTubePlayer = player
                    }
                })
            }
        },
        onRelease = { view ->
            youTubePlayer = null
            lifecycleOwner.lifecycle.removeObserver(view)
            view.release()
        }
    )

    LaunchedEffect(youTubePlayer, videoId, playToken) {
        val player = youTubePlayer ?: return@LaunchedEffect

        if (playToken < 0) {
            player.cueVideo(videoId, 0f)
        } else {
            player.loadVideo(videoId, 0f)
        }
    }
}

fun extractId(text : String) : String {
    val value = text.trim()
    if (value.isEmpty()) return ""

    youtubeIdPattern.find(value)?.let { match ->
        return match.groupValues[1]
    }

    return if (rawYoutubeIdPattern.matches(value)) value else ""
}