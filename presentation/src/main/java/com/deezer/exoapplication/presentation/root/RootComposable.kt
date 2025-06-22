package com.deezer.exoapplication.presentation.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.deezer.exoapplication.presentation.player.PlayerScreen
import com.deezer.exoapplication.presentation.theme.ExoAppTheme

@Composable
fun RootComposable() {
    ExoAppTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            // Here we would typically include the app navigation
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                PlayerScreen()
            }
        }
    }
}
