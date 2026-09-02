package com.m4ykey.album.presentation.detail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.m4ykey.album.R
import com.m4ykey.core.ui.ActionIconButton
import com.m4ykey.core.ui.AppScaffold

@Composable
fun AlbumCoverScreen(
    modifier: Modifier = Modifier,
    imageUrl : String,
    onBack : () -> Unit
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
                imageUrl = imageUrl
            )
        }
    )
}

@Composable
fun CoverDisplay(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    imageUrl : String
) {

}

@Preview
@Composable
private fun CoverDisplayPrev() {
    CoverDisplay(
        paddingValues = PaddingValues(10.dp),
        imageUrl = ""
    )
}