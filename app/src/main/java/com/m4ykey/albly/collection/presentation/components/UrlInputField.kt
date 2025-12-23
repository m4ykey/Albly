package com.m4ykey.albly.collection.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.m4ykey.albly.R
import com.m4ykey.core.ext.ActionIconButton

@Composable
fun UrlInputField(
    state : TextFieldState
) {
    OutlinedTextField(
        state = state,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        leadingIcon = {
            Icon(
                contentDescription = null,
                painter = painterResource(R.drawable.ic_link)
            )
        },
        label = { Text(stringResource(R.string.enter_url)) },
        trailingIcon = {
            if (state.text.isNotEmpty()) {
                ActionIconButton(
                    onClick = { state.edit { replace(0, state.text.length, "") } },
                    iconRes = R.drawable.ic_close,
                    textRes = R.string.empty
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Uri
        )
    )
}