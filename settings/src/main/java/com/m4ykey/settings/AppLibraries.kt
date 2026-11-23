package com.m4ykey.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import com.m4ykey.core.ui.ActionIconButton
import com.m4ykey.core.ui.AppScaffold
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.variant.LibrariesDensity
import com.mikepenz.aboutlibraries.ui.compose.variant.LibrariesVariant
import org.koin.compose.koinInject

@Composable
fun AppLibraries(
    modifier: Modifier = Modifier,
    onBack : () -> Unit,
    provider : LibrariesProvider = koinInject()
) {
    val libraries by produceState(initialValue = provider.cached) {
        value = provider.get()
    }

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
            val libs = libraries
            if (libs != null) {
                LibrariesContainer(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    libraries = libs,
                    variant = LibrariesVariant.Refined,
                    density = LibrariesDensity.Compact
                )
            }
        },
        title = R.string.used_libraries
    )
}