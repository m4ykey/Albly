package com.m4ykey.album.presentation.video

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.m4ykey.album.R
import com.m4ykey.core.ui.ActionIconButton
import com.m4ykey.core.ui.AppScaffold
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import org.koin.androidx.compose.koinViewModel

@Composable
fun AlbumVideoScreen(
    modifier: Modifier = Modifier,
    onBack : () -> Unit,
    videos : List<String>,
    viewModel: AlbumVideoViewModel = koinViewModel()
) {

    LaunchedEffect(videos) {
        viewModel.setVideos(videos)
    }

    val videoList by viewModel.videos.collectAsStateWithLifecycle()

    AppScaffold(
        content = { padding ->
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(videoList) { video ->
                    YoutubeScreen(
                        videoId = extractId(video),
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                    )
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
fun YoutubeScreen(
    modifier: Modifier = Modifier,
    videoId : String
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        AndroidView(
            modifier = modifier,
            factory = { context ->
                YouTubePlayerView(context).apply {
                    lifecycleOwner.lifecycle.addObserver(this)

                    addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            super.onReady(youTubePlayer)
                            youTubePlayer.cueVideo(videoId = videoId, 0f)
                        }
                    })
                }
            },
            update = { view ->
                view.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
                    override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.cueVideo(videoId, 0f)
                    }
                })
            }
        )
    }
}

fun extractId(text : String) : String {
    if (text.isBlank()) return ""

    return when {
        text.contains("youtu.be/") -> {
            text.substringAfter("youtu.be/")
                .substringBefore("?")
                .substringBefore("&")
        }

        text.contains("watch?v=") -> {
            text.substringAfter("watch?v=")
                .substringBefore("&")
                .substringBefore("?")
        }

        text.contains("/embed/") -> {
            text.substringAfter("/embed/")
                .substringBefore("?")
                .substringBefore("&")
        }

        else -> ""
    }
}