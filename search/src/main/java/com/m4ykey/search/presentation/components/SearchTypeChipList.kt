package com.m4ykey.search.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.m4ykey.core.ui.CenteredContent
import com.m4ykey.core.ui.ChipItem
import com.m4ykey.search.R
import com.m4ykey.search.presentation.SearchType

@Composable
fun SearchTypeChipList(
    modifier: Modifier = Modifier,
    onChipSelected: (SearchType) -> Unit,
    selectedChip: SearchType
) {

    val chipList = SearchType.entries.map { type -> type.getLabel() to type }

    CenteredContent(modifier = modifier) { contentModifier ->
        LazyRow(modifier = contentModifier.padding(horizontal = 5.dp)) {
            items(chipList) { (label, sortKey) ->
                ChipItem(
                    selected = selectedChip == sortKey,
                    label = label,
                    onSelect = {
                        onChipSelected(sortKey)
                    },
                    modifier = Modifier.padding(horizontal = 5.dp),
                    isTrailingIcon = true
                )
            }
        }
    }
}

@Composable
fun SearchType.getLabel(): String {
    return when (this) {
        SearchType.ALBUM -> stringResource(id = R.string.album)
        SearchType.ARTIST -> stringResource(id = R.string.artist)
        SearchType.LYRICS -> stringResource(id = R.string.lyrics)
    }
}