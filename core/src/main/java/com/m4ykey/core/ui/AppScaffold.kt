package com.m4ykey.core.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource

@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    @StringRes title : Int? = null,
    actions : @Composable RowScope.() -> Unit = {},
    navigation : @Composable () -> Unit = {},
    content : @Composable (PaddingValues) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    snackbarHostState: SnackbarHostState? = null,
    transparentTopBar : Boolean = false,
    topBarContentColor : Color = Color.Unspecified
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { if (title != null) Text(text = stringResource(title)) },
                navigationIcon = {
                    navigation()
                },
                actions = {
                    actions()
                },
                scrollBehavior = scrollBehavior,
                colors = if (transparentTopBar) {
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent,
                        navigationIconContentColor = topBarContentColor,
                        actionIconContentColor = topBarContentColor,
                        titleContentColor = topBarContentColor
                    )
                } else {
                    TopAppBarDefaults.topAppBarColors()
                }
            )
        },
        snackbarHost = {
            snackbarHostState?.let { SnackbarHost(hostState = it) }
        }
    ) { padding ->
        content(padding)
    }
}