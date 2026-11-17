package com.m4ykey.search.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m4ykey.search.R

@Composable
fun SearchItemList(
    modifier: Modifier = Modifier,
    text : String,
    onClick : () -> Unit,
    onLongClick : () -> Unit,
    onSetText : (String) -> Unit
) {
    Row(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            contentDescription = null,
            painter = painterResource(R.drawable.ic_recent),
            modifier = Modifier.size(21.dp)
        )
        Text(
            text = text,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .weight(1f),
            fontSize = 16.sp
        )
        Icon(
            contentDescription = null,
            painter = painterResource(R.drawable.ic_arrow_recent),
            modifier = Modifier
                .size(21.dp)
                .clickable { onSetText(text) }
        )
    }
}

@Preview
@Composable
private fun SearchItemListPrev() {
    SearchItemList(
        text = "Search",
        onClick = {},
        onLongClick = {},
        onSetText = {}
    )
}