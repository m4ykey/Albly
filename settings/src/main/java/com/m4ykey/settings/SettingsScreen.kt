@file:OptIn(ExperimentalMaterial3Api::class)

package com.m4ykey.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.m4ykey.core.ext.ActionIconButton
import com.m4ykey.core.ext.AppScaffold
import com.m4ykey.settings.theme.ThemeType
import com.m4ykey.settings.theme.label

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
                        onClick = {  }
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
        },
        title = R.string.settings
    )
}

@Composable
fun OpenTheme(
    modifier: Modifier = Modifier,
    isDialogVisible : Boolean,
    onDismissDialog : () -> Unit,
    onChange : (ThemeType) -> Unit
) {
    var currentType by remember { mutableStateOf(ThemeType.DEFAULT) }

    if (isDialogVisible) {
        AnimatedVisibility(
            visible = isDialogVisible,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            BasicAlertDialog(
                onDismissRequest = onDismissDialog,
                properties = DialogProperties(dismissOnBackPress = true)
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 6.dp,
                    modifier = modifier.padding(16.dp)
                ) {
                    Column(modifier = modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(R.string.theme),
                            modifier = modifier.padding(bottom = 8.dp),
                            fontSize = 20.sp
                        )

                        ThemeType.entries.forEach { type ->
                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        currentType = type
                                        onChange(type)
                                        onDismissDialog()
                                    }
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = currentType == type,
                                    onClick = {
                                        currentType = type
                                        onChange(type)
                                        onDismissDialog()
                                    }
                                )
                                Text(
                                    text = type.label(),
                                    modifier = modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsIcon(
    modifier: Modifier = Modifier,
    icon : Int,
    title : Int,
    message : Int,
    onClick : () -> Unit
) {
    val textColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    val textColor2 = if (isSystemInDarkTheme()) Color(0xFFBDBDBD) else Color(0xFF424242)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = stringResource(title),
            modifier = modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = modifier.weight(1f)
        ) {
            Text(
                text = stringResource(title),
                fontSize = 17.sp,
                color = textColor
            )
            Text(
                text = stringResource(message),
                color = textColor2,
                fontSize = 14.sp
            )
        }
        Icon(
            contentDescription = null,
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight
        )
    }
}