@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.m4ykey.core.ext

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.crossfade.CrossfadePlugin

@Composable
fun LoadImage(
    modifier: Modifier = Modifier,
    imageUrl : String
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
    ) {
        CoilImage(
            component = rememberImageComponent {
                CrossfadePlugin(
                    duration = 500
                )
            },
            loading = {
                Box(modifier = Modifier.matchParentSize()) {
                    ContainedLoadingIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(32.dp)
                    )
                }
            },
            imageModel = { imageUrl },
            imageOptions = ImageOptions(
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop
            )
        )
    }
}