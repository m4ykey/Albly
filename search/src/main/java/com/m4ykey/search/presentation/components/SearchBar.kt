package com.m4ykey.search.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.m4ykey.core.ui.ActionIconButton
import com.m4ykey.search.R
import com.m4ykey.search.presentation.SearchAction

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    isDarkTheme: Color,
    searchQuery : String,
    onAction: (SearchAction) -> Unit,
    handleMick : () -> Unit
) {
    val targetWeight = if (searchQuery.isEmpty()) 1f else 0.8f

    val animatedWeight by animateFloatAsState(
        label = "SearchBarWeightAnimation",
        targetValue = targetWeight,
        animationSpec = tween(durationMillis = 1000)
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ActionIconButton(
            tint = isDarkTheme,
            onClick = onBack,
            textRes = com.m4ykey.core.R.string.back,
            iconRes = com.m4ykey.core.R.drawable.ic_arrow_left
        )
        SearchBarTextField(
            searchQuery = searchQuery,
            onValueChange = { query ->
                onAction(SearchAction.OnQueryChange(query))
            },
            modifier = Modifier
                .weight(animatedWeight)
                .padding(start = 8.dp),
            onSearch = {
                onAction(SearchAction.OnSearchClick)
                onAction(SearchAction.OnSearchItemClick(searchQuery))
            }
        )
        ActionIconButton(
            tint = isDarkTheme,
            onClick = { handleMick() },
            textRes = R.string.mic,
            iconRes = R.drawable.ic_mic
        )
        AnimatedVisibility(
            visible = searchQuery.isNotEmpty(),
            enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth }
            ) + expandHorizontally(),
            exit = slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth }
            ) + shrinkHorizontally()
        ) {
            ActionIconButton(
                onClick = { onAction(SearchAction.OnQueryChange("")) },
                tint = isDarkTheme,
                textRes = R.string.clear,
                iconRes = R.drawable.ic_close
            )
        }
    }
}