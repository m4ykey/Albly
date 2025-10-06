package com.m4ykey.albly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.m4ykey.albly.collection.presentation.CollectionScreen
import com.m4ykey.albly.news.presentation.NewsScreen
import com.m4ykey.albly.ui.navigation.CollectionScreen
import com.m4ykey.albly.ui.navigation.NavigationViewModel
import com.m4ykey.albly.ui.navigation.NewsScreen
import com.m4ykey.albly.ui.theme.AlblyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlblyTheme {
                val bottomNavItems = listOf(CollectionScreen, NewsScreen)
                val navigationViewModel : NavigationViewModel = viewModel()
                val navStack by navigationViewModel.navStack.collectAsState()

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            bottomNavItems.forEach { item ->
                                val selected = navStack.topLevelKey == item
                                NavigationBarItem(
                                    selected = selected,
                                    onClick = {
                                        navigationViewModel.switchTopLevel(item)
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = item.icon,
                                            contentDescription = item.title
                                        )
                                    },
                                    label = {
                                        Text(text = item.title)
                                    }
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    val screenModifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)

                    NavDisplay(
                        backStack = navStack.backStack,
                        onBack = { navigationViewModel.goBack() },
                        entryProvider = entryProvider {
                            entry<CollectionScreen> {
                                CollectionScreen(modifier = screenModifier)
                            }
                            entry<NewsScreen> { NewsScreen() }
                        }
                    )
                }
            }
        }
    }
}