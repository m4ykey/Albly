package com.m4ykey.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.m4ykey.core.ui.ActionIconButton
import com.m4ykey.core.ui.AppScaffold
import com.mikepenz.aboutlibraries.ui.compose.android.produceLibraries
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer

@Composable
fun AppLibraries(
    modifier: Modifier = Modifier,
    onBack : () -> Unit
) {
    val libraries by produceLibraries()

    AppScaffold(
        modifier = modifier,
        navigation = {
            ActionIconButton(
                onClick = onBack,
                iconRes = R.drawable.ic_arrow_left,
                textRes = com.m4ykey.core.R.string.back
            )
        },
        content = { padding ->
            LibrariesContainer(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                libraries = libraries
            )
        }
    )
}