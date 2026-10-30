package com.m4ykey.settings

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.ImageLoader
import coil3.imageLoader
import com.m4ykey.core.network.openLink
import com.m4ykey.core.ui.ActionIconButton
import com.m4ykey.core.ui.AppScaffold
import com.m4ykey.settings.theme.ThemeType
import com.m4ykey.settings.theme.label
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@ExperimentalMaterial3Api
@Composable
fun SettingsScreen(
    onBack : () -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val state = rememberLazyListState()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

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
            SettingsContent(
                state = state,
                themeLabel = selectedTheme.label(),
                paddingValues = padding,
                onOpenSourceClick = { openLink("https://github.com/m4ykey/Albly", context = context) },
                onThemeClick = { isThemeDialogVisible = true },
                onImageCacheClick = {
                    scope.launch {
                        clearImageCache(context.imageLoader)
                    }
                }
            )
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

suspend fun clearImageCache(imageLoader: ImageLoader) {
    imageLoader.memoryCache?.clear()
    imageLoader.diskCache?.clear()
}

@Composable
fun SettingsContent(
    modifier: Modifier = Modifier,
    state : LazyListState,
    paddingValues: PaddingValues,
    themeLabel : String,
    onThemeClick : () -> Unit,
    onOpenSourceClick : () -> Unit,
    onImageCacheClick : () -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(paddingValues),
        state = state
    ) {
        item {
            SettingsIcon(
                icon = R.drawable.moon,
                title = R.string.theme,
                message = themeLabel,
                onClick = onThemeClick
            )
        }
        item {
            SettingsIcon(
                icon = R.drawable.github,
                title = R.string.open_source,
                message = stringResource(R.string.open_source_message),
                onClick = onOpenSourceClick
            )
        }
        item {
            SettingsIcon(
                icon = R.drawable.ic_image,
                title = R.string.remove_image_cache,
                message = stringResource(R.string.remove_image_cache_message),
                onClick = onImageCacheClick
            )
        }
        item {
            ContentProviders(modifier = Modifier.padding(horizontal = 10.dp))
        }
        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            Text(
                text = BuildConfig.APP_VERSION,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
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

@Composable
fun ContentProviders(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        ProviderSection(
            providers = dataProviders,
            onProviderClick = { openLink(it.url, context) },
            title = stringResource(R.string.data_is_provided_by)
        )
        ProviderSection(
            providers = lyricsProvider,
            onProviderClick = { openLink(it.url, context) },
            title = stringResource(R.string.lyrics_data_is_provided_by)
        )
        ProviderSection(
            providers = searchProvider,
            onProviderClick = { openLink(it.url, context) },
            title = stringResource(R.string.song_search_engine)
        )
    }
}

@Composable
private fun ProviderSection(
    title: String,
    modifier: Modifier = Modifier,
    providers : List<Provider>,
    onProviderClick : (Provider) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            providers.forEach { provider ->
                ContentItem(
                    text = provider.name,
                    image = provider.image,
                    modifier = Modifier.weight(1f),
                    onItemClick = { onProviderClick(provider) }
                )
            }
        }
    }
}

@Composable
fun ContentItem(
    text: String,
    @DrawableRes image: Int,
    modifier: Modifier = Modifier,
    onItemClick : () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        onClick = onItemClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}