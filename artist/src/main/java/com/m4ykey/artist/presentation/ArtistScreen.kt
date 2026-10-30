@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.m4ykey.artist.presentation

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.get
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.asDrawable
import coil3.request.ImageRequest
import coil3.request.allowHardware
import com.m4ykey.artist.R
import com.m4ykey.core.network.openLink
import com.m4ykey.core.ui.ActionIconButton
import com.m4ykey.core.ui.AppScaffold
import com.m4ykey.core.ui.ErrorCard
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import com.skydoves.landscapist.coil3.CoilImageState
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.crossfade.CrossfadePlugin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel

@Composable
fun ArtistScreen(
    modifier: Modifier = Modifier,
    artistId: Int,
    onBack: () -> Unit,
    viewModel: ArtistViewModel = koinViewModel()
) {
    LaunchedEffect(artistId) {
        viewModel.loadArtists(listOf(artistId))
    }

    val state by viewModel.artistDialogState.collectAsStateWithLifecycle()

    var topIsLight by remember { mutableStateOf(false) }

    val view = LocalView.current
    DisposableEffect(topIsLight) {
        val window = (view.context as Activity).window
        val controller = WindowCompat.getInsetsController(window, view)
        val previous = controller.isAppearanceLightStatusBars
        controller.isAppearanceLightStatusBars = topIsLight
        onDispose { controller.isAppearanceLightStatusBars = previous }
    }

    AppScaffold(
        content = { padding ->
            ArtistContent(
                state = state,
                modifier = Modifier.padding(bottom = padding.calculateBottomPadding()),
                onTopLightnessDetected = { topIsLight = it }
            )
        },
        navigation = {
            ActionIconButton(
                onClick = onBack,
                iconRes = com.m4ykey.core.R.drawable.ic_arrow_left,
                textRes = com.m4ykey.core.R.string.back
            )
        },
        transparentTopBar = true,
        topBarContentColor = if (topIsLight) Color.Black else Color.White
    )
}

@Composable
fun ArtistContent(
    modifier: Modifier = Modifier,
    state: ArtistState?,
    onTopLightnessDetected: (isLight: Boolean) -> Unit
) {
    when (state) {
        is ArtistState.Success -> {
            val artist = state.artists.firstOrNull() ?: return
            val imageUrl = artist.images.find { it.type == "primary" }?.uri ?: artist.images.firstOrNull()?.uri

            val context = LocalContext.current

            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
            ) {
                item {
                    DisplayArtist(
                        artistName = artist.name,
                        artistImage = imageUrl.orEmpty(),
                        onTopLightnessDetected = onTopLightnessDetected,
                        onArtistClick = { openLink(artist.uri, context) },
                        id = artist.id,
                        realname = artist.realname
                    )
                }
            }
        }
        is ArtistState.Error -> {
            ErrorCard(text = state.message)
        }
        is ArtistState.Loading -> {
            Box(modifier = Modifier
                .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ContainedLoadingIndicator()
            }
        }
        null -> {}
    }
}

@Composable
fun DisplayArtist(
    modifier: Modifier = Modifier,
    artistName : String,
    artistImage : String,
    onTopLightnessDetected: (isLight : Boolean) -> Unit = {},
    onArtistClick : () -> Unit,
    realname : String?,
    id : Int
) {
    val resources = LocalResources.current
    val context = LocalContext.current

    var loadedDrawable by remember { mutableStateOf<Drawable?>(null) }

    LaunchedEffect(loadedDrawable) {
        loadedDrawable?.let { drawable ->
            val isLight = withContext(Dispatchers.Default) { drawable.isTopLight() }
            onTopLightnessDetected(isLight)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(375.dp)
    ) {
        CoilImage(
            imageRequest = {
                ImageRequest.Builder(context)
                    .data(artistImage)
                    .allowHardware(false)
                    .build()
            },
            imageOptions = ImageOptions(
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop
            ),
            component = rememberImageComponent {
                CrossfadePlugin(duration = 500)
            },
            modifier = Modifier.fillMaxSize(),
            onImageStateChanged = { state ->
                if (state is CoilImageState.Success) {
                    loadedDrawable = state.image?.asDrawable(resources)
                }
            }
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(120.dp)
                .background(
                    Brush.verticalGradient(
                        0f to Color.Black.copy(alpha = 0.5f),
                        1f to Color.Transparent
                    )
                )
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            0f to Color.Transparent,
                            1f to Color.Black.copy(alpha = 0.8f)
                        )
                    )
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xff282A2D)
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "#",
                        color = Color(0xffFFC665),
                        fontSize = 11.sp
                    )
                    Text(
                        fontWeight = FontWeight.SemiBold,
                        text = "ID #$id",
                        fontSize = 11.sp,
                        color = Color(0xffD4C4B0)
                    )
                }
            }
            Text(
                text = artistName,
                color = Color(0xffE2E2E6),
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp
            )
            realname?.let {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        contentDescription = "",
                        painter = painterResource(R.drawable.ic_realname),
                        tint = Color(0xffFFC665)
                    )
                    Text(
                        text = "${stringResource(R.string.realname)}:",
                        fontSize = 14.sp,
                        color = Color(0xFFD4C4B0)
                    )
                    Text(
                        color = Color.White,
                        text = realname
                    )
                }
            }
            Button(
                onClick = onArtistClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFC665)
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_album),
                        contentDescription = null,
                        tint = Color(0xFF432C00)
                    )
                    Text(
                        text = stringResource(R.string.discover_the_artist),
                        fontSize = 14.sp,
                        color = Color(0xFF432C00)
                    )
                    Icon(
                        contentDescription = null,
                        painter = painterResource(R.drawable.ic_arrow_up_right),
                        modifier = Modifier.size(24.dp),
                        tint = Color(0xFF432C00)
                    )
                }
            }
        }
    }
}

fun Drawable.isTopLight() : Boolean {
    val bitmap = toBitmap(width = 32, height = 32)
    var sum = 0f
    var count = 0
    for (y in 0 until 8) {
        for (x in 0 until 32) {
            sum += Color(bitmap[x, y]).luminance()
            count++
        }
    }
    return sum / count > 0.5f
}