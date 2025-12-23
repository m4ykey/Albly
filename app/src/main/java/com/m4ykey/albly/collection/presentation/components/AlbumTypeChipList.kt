package com.m4ykey.albly.collection.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.m4ykey.albly.collection.presentation.type.album.AlbumType
import com.m4ykey.core.chip.ChipItem
import com.m4ykey.core.ext.CenteredContent

@Composable
fun AlbumTypeChipList(
    modifier : Modifier = Modifier,
    onChipSelected : (AlbumType?) -> Unit,
    selectedChip : AlbumType?
) {
    val chipList = AlbumType.entries
        .filter { it != AlbumType.EMPTY }
        .map { type -> type.getLabel() to type }

    CenteredContent(modifier = modifier) { contentModifier ->
        LazyRow(modifier = contentModifier.padding(horizontal = 5.dp)) {
            items(chipList) { (label, sortKey) ->
                ChipItem(
                    modifier = Modifier.padding(horizontal = 5.dp),
                    selected = selectedChip == sortKey,
                    label = label,
                    onSelect = {
                        if (selectedChip == sortKey) {
                            onChipSelected(null)
                        } else {
                            onChipSelected(sortKey)
                        }
                    },
                    isLeadingIcon = true
                )
            }
        }
    }
}