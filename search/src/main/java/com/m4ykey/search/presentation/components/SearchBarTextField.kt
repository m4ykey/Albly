package com.m4ykey.search.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.m4ykey.search.R

@Composable
fun SearchBarTextField(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    searchQuery: String,
    onSearch: () -> Unit
) {
    val searchFieldBackground = RoundedCornerShape(100.dp)
    val containerColor = MaterialTheme.colorScheme.surfaceContainer

    Row(
        modifier = modifier
            .height(36.dp)
            .clip(searchFieldBackground)
            .background(containerColor)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_search),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(size = 20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            contentAlignment = Alignment.CenterStart
        ) {
            SearchInputTextField(
                searchQuery = searchQuery,
                onValueChange = onValueChange,
                onSearch = onSearch
            )
        }
    }
}