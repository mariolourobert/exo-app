package com.deezer.exoapplication.presentation.player

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import org.koin.androidx.compose.koinViewModel

// For this exercise, we will use a hardcoded playlist Id:
private const val PLAYLIST_ID = 1

private val TRACK_HEIGHT = 56.dp

@Composable
fun PlayerScreen() {
    val viewModel = koinViewModel<PlayerScreenViewModel>()

    LaunchedEffect(Unit) {
        viewModel.fetchDataIfNecessary(
            playlistId = PLAYLIST_ID,
        )
    }

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when (state) {
        is PlayerScreenUiState.Loading ->
            LoadingState()

        is PlayerScreenUiState.Error ->
            ErrorState(uiState = state as PlayerScreenUiState.Error)

        is PlayerScreenUiState.Loaded ->
            LoadedState(uiState = state as PlayerScreenUiState.Loaded)
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(
    uiState: PlayerScreenUiState.Error,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = uiState.message.asString(),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun LoadedState(
    uiState: PlayerScreenUiState.Loaded,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Player(
            modifier = Modifier.weight(0.3f),
            uiState = uiState,
        )
        TracksList(
            modifier = Modifier.weight(0.7f),
            uiState = uiState,
        )
    }
}

@OptIn(UnstableApi::class)
@Composable
private fun Player(
    modifier: Modifier = Modifier,
    uiState: PlayerScreenUiState.Loaded,
) {
    AndroidView(
        modifier = modifier
            .fillMaxWidth(),
        factory = { context ->
            PlayerView(context).apply {
                setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                setShowShuffleButton(true)
                val player = ExoPlayer.Builder(context).build()
                setPlayer(player)
                player.setMediaItem(MediaItem.fromUri("https://filesamples.com/samples/audio/mp3/sample1.mp3"))
                player.prepare()
                player.play()
            }
        }
    )
}

@Composable
private fun TracksList(
    modifier: Modifier = Modifier,
    uiState: PlayerScreenUiState.Loaded,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
    ) {
        items(uiState.tracks) { trackUiModel ->
            Track(trackUiModel = trackUiModel)
        }
    }
}

@Composable
private fun Track(
    trackUiModel: PlayerScreenUiState.TrackUiModel,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(TRACK_HEIGHT)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp), // Should use something like MaterialTheme.padding.SizeM
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = trackUiModel.trackName,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        HorizontalDivider()
    }
}
