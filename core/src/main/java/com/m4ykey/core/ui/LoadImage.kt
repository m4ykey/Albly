package com.m4ykey.core.ui

import android.graphics.Bitmap
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import com.skydoves.landscapist.coil3.CoilImageState
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.crossfade.CrossfadePlugin

@Composable
fun LoadImage(
    modifier: Modifier = Modifier,
    imageUrl : String,
    onImageLoaded : ((Bitmap) -> Unit)? = null
) {
    var imageState by remember {
        mutableStateOf<CoilImageState>(CoilImageState.None)
    }

    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
    ) {
        CoilImage(
            imageModel = { imageUrl },
            imageOptions = ImageOptions(
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop
            ),
            component = rememberImageComponent {
                CrossfadePlugin(duration = 500)
            },
            onImageStateChanged = {
                imageState = it
            },
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxSize()
        )

        val currentImageState = imageState

        if (currentImageState is CoilImageState.Success) {
            val imageBitmap = currentImageState.imageBitmap

            LaunchedEffect(imageBitmap) {
                imageBitmap?.let {
                    onImageLoaded?.invoke(it.asAndroidBitmap())
                }
            }
        }
    }
}