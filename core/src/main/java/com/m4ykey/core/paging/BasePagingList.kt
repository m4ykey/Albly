@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.m4ykey.core.paging

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.m4ykey.core.R
import com.m4ykey.core.ui.AnimationCard
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

private val defaultLoading : @Composable () -> Unit = { DefaultLoading() }
private val defaultError : @Composable (Throwable?, onRetry : () -> Unit) -> Unit = { error, retry ->
    DefaultError(error, retry)
}
private val defaultEmpty : @Composable () -> Unit = { DefaultEmpty() }

@Composable
fun ErrorItem(message : String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall
        )
        TextButton(onClick = onRetry) {
            Text(text = "Retry")
        }
    }
}

@Composable
private fun DefaultEmpty() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AnimationCard(animation = R.raw.cat_empty_list, showText = false)
            Text(
                text = stringResource(R.string.no_results_found),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun DefaultLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ContainedLoadingIndicator()
    }
}

@Composable
private fun DefaultError(error : Throwable?, onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AnimationCard(error?.message ?: "Unexpected error occurred")
            Button(onClick = onRetry) {
                Text(stringResource(R.string.try_again))
            }
        }
    }
}

@Composable
fun <T: Any> BasePagingList(
    items : LazyPagingItems<T>,
    loadingContent : @Composable () -> Unit = defaultLoading,
    errorContent : @Composable (Throwable?, onRetry : () -> Unit) -> Unit = defaultError,
    emptyContent : @Composable () -> Unit = defaultEmpty,
    listContent : @Composable (LazyPagingItems<T>) -> Unit,
    isActiveSearch : Boolean = true
) {
    var showLoading by remember { mutableStateOf(false) }

    val refreshState = items.loadState.refresh
    val isRefreshing = refreshState is LoadState.Loading

    if (!isActiveSearch) {
        return
    }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            showLoading = false
            delay(300L.milliseconds)
            showLoading = true
        } else {
            showLoading = false
        }
    }

    when (refreshState) {
        is LoadState.Loading -> {
            if (showLoading || items.itemCount == 0) loadingContent()
        }

        is LoadState.Error -> {
            errorContent(refreshState.error) { items.retry() }
        }

        is LoadState.NotLoading -> {
            if (items.itemCount == 0) {
                emptyContent()
            } else {
                listContent(items)
            }
        }
    }
}