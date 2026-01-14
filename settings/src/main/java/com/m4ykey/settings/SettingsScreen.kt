package com.m4ykey.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.m4ykey.core.ext.ActionIconButton
import com.m4ykey.core.ext.AppScaffold

@ExperimentalMaterial3Api
@Composable
fun SettingsScreen(
    onBack : () -> Unit
) {
    val state = rememberLazyListState()

    AppScaffold(
        navigation = {
            ActionIconButton(
                onClick = onBack,
                iconRes = R.drawable.ic_arrow_left,
                textRes = com.m4ykey.core.R.string.back
            )
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier.padding(padding),
                state = state
            ) {
                item {
                    SettingsIcon(
                        icon = R.drawable.moon,
                        title = R.string.theme,
                        message = R.string.theme,
                        onClick = {}
                    )
                }
                item {
                    SettingsIcon(
                        icon = R.drawable.github,
                        title = R.string.open_source,
                        message = R.string.open_source_message,
                        onClick = {}
                    )
                }
            }
        }
    )
}

@Composable
fun SettingsIcon(
    modifier: Modifier = Modifier,
    icon : Int,
    title : Int,
    message : Int,
    onClick : () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = stringResource(title),
            modifier = modifier.size(24.dp)
        )
        Column(
            modifier = modifier.weight(1f)
        ) {
            Text(text = stringResource(title))
            Text(text = stringResource(message))
        }
        Icon(
            contentDescription = null,
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight
        )
    }
}