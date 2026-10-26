package com.m4ykey.settings

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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.m4ykey.core.network.openLink
import com.m4ykey.core.ui.ActionIconButton
import com.m4ykey.core.ui.AppScaffold
import com.m4ykey.settings.theme.ThemeType
import com.m4ykey.settings.theme.label
import org.koin.compose.viewmodel.koinViewModel

@ExperimentalMaterial3Api
@Composable
fun SettingsScreen(
    onBack : () -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val state = rememberLazyListState()

    val context = LocalContext.current

    var isThemeDialogVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val selectedTheme by viewModel.selectedTheme.collectAsStateWithLifecycle()

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
                        message = selectedTheme.label(),
                        onClick = { isThemeDialogVisible = true }
                    )
                }
                item {
                    SettingsIcon(
                        icon = R.drawable.github,
                        title = R.string.open_source,
                        message = stringResource(R.string.open_source_message),
                        onClick = { openLink("https://github.com/m4ykey/Albly", context = context) }
                    )
                }
            }
        },
        title = R.string.settings
    )

    OpenTheme(
        onDismissDialog = { isThemeDialogVisible = false },
        isDialogVisible = isThemeDialogVisible,
        onChange = { type ->
            viewModel.changeTheme(type)
        },
        currentType = selectedTheme
    )
}

@Composable
fun OpenTheme(
    modifier: Modifier = Modifier,
    isDialogVisible : Boolean,
    onDismissDialog : () -> Unit,
    onChange : (ThemeType) -> Unit,
    currentType: ThemeType
) {
    if (!isDialogVisible) return

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
                                onChange(type)
                                onDismissDialog()
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentType == type,
                            onClick = {
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

@Composable
fun SettingsIcon(
    modifier: Modifier = Modifier,
    icon : Int,
    title : Int,
    message : String,
    onClick : () -> Unit
) {
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
                fontSize = 17.sp
            )
            Text(
                text = message,
                fontSize = 14.sp
            )
        }
        Icon(
            contentDescription = null,
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight
        )
    }
}