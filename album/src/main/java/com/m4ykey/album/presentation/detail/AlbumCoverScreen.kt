package com.m4ykey.album.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.m4ykey.album.R
import com.m4ykey.core.ui.ActionIconButton
import com.m4ykey.core.ui.AppScaffold
import com.m4ykey.core.ui.LoadImage
import com.m4ykey.core.ui.copyText
import com.m4ykey.core.ui.showToast
import org.koin.androidx.compose.koinViewModel

@Composable
fun AlbumCoverScreen(
    modifier: Modifier = Modifier,
    imageUrl : String,
    onBack : () -> Unit,
    viewModel: CoverViewModel = koinViewModel()
) {
    AppScaffold(
        navigation = {
            ActionIconButton(
                onClick = onBack,
                textRes = R.string.back,
                iconRes = R.drawable.ic_arrow_left
            )
        },
        content = { padding ->
            CoverDisplay(
                paddingValues = padding,
                imageUrl = imageUrl,
                modifier = modifier,
                viewModel = viewModel
            )
        }
    )
}

@Composable
fun CoverDisplay(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    imageUrl : String,
    viewModel: CoverViewModel
) {
    val colors by viewModel.colors.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Column(
        modifier = modifier.padding(paddingValues),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            LoadImage(
                imageUrl = imageUrl,
                modifier = Modifier.size(260.dp),
                onImageLoaded = viewModel::extractColors
            )
        }
        LazyHorizontalGrid(
            rows = GridCells.Fixed(2),
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
                .height(110.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(colors) { color ->
                Card(
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier
                        .clickable {
                            val hex = String.format("#%06X", color and 0xFFFFFF)
                            copyText(hex, context = context)
                            showToast(context = context, hex)
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color(color))
                            .size(50.dp)
                    )
                }
            }
        }
    }
}