package com.deezer.exoapplication.presentation.player

import androidx.annotation.OptIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.deezer.exoapplication.utils.collectWithLifecycle
import org.koin.androidx.compose.koinViewModel

// For this exercise, we will use a hardcoded playlist Id:
private const val PLAYLIST_ID = 1

private val TRACK_HEIGHT = 56.dp

@Composable
fun PlayerScreen() {
    val viewModel = koinViewModel<PlayerScreenViewModel>()
    val context = LocalContext.current
    val exoPlayer: ExoPlayer = remember {
        ExoPlayer.Builder(context)
            .build()
    }
    val exoPlayerListener = remember {
        object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if (playbackState == Player.STATE_ENDED) {
                    viewModel.onIntent(
                        intent = PlayerScreenViewModelIntent.OnCurrentTrackFinished,
                    )
                }
            }
        }
    }
    DisposableEffect(Unit) {
        exoPlayer.addListener(exoPlayerListener)
        onDispose {
            exoPlayer.removeListener(exoPlayerListener)
        }
    }

    viewModel.event.collectWithLifecycle { event ->
        when (event) {
            is PlayerScreenEvent.PlayTrack -> {
                exoPlayer.setMediaItem(
                    MediaItem.fromUri(event.trackUri),
                )
                exoPlayer.prepare()
                exoPlayer.play()
            }

            PlayerScreenEvent.UnloadAllTracks -> {
                exoPlayer.clearMediaItems()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchDataIfNecessary(
            playlistId = PLAYLIST_ID,
        )
    }

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val onTrackClick = remember {
        { trackUiModel: PlayerScreenUiState.TrackUiModel ->
            viewModel.onIntent(
                intent = PlayerScreenViewModelIntent.OnTrackClick(
                    trackId = trackUiModel.uid,
                ),
            )
        }
    }

    val onRemoveTrackClick = remember {
        { trackUiModel: PlayerScreenUiState.TrackUiModel ->
            viewModel.onIntent(
                intent = PlayerScreenViewModelIntent.OnRemoveTrackClick(
                    trackId = trackUiModel.uid,
                ),
            )
        }
    }

    when (state) {
        is PlayerScreenUiState.Loading ->
            LoadingState()

        is PlayerScreenUiState.Error ->
            ErrorState(uiState = state as PlayerScreenUiState.Error)

        is PlayerScreenUiState.Loaded ->
            LoadedState(
                uiState = state as PlayerScreenUiState.Loaded,
                exoPlayer = exoPlayer,
                onTrackClick = onTrackClick,
                onRemoveTrackClick = onRemoveTrackClick,
            )
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
    exoPlayer: ExoPlayer,
    onTrackClick: (PlayerScreenUiState.TrackUiModel) -> Unit,
    onRemoveTrackClick: (PlayerScreenUiState.TrackUiModel) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Player(
            modifier = Modifier.weight(0.3f),
            uiState = uiState,
            exoPlayer = exoPlayer,
        )
        TracksList(
            modifier = Modifier.weight(0.7f),
            uiState = uiState,
            onTrackClick = onTrackClick,
            onRemoveTrackClick = onRemoveTrackClick,
        )
    }
}

@OptIn(UnstableApi::class)
@Composable
private fun Player(
    modifier: Modifier = Modifier,
    uiState: PlayerScreenUiState.Loaded,
    exoPlayer: ExoPlayer,
) {
    AndroidView(
        modifier = modifier
            .fillMaxWidth(),
        factory = { context ->
            PlayerView(context).apply {
                setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                setShowShuffleButton(true)
                setPlayer(exoPlayer)
            }
        }
    )
}

@Composable
private fun TracksList(
    modifier: Modifier = Modifier,
    uiState: PlayerScreenUiState.Loaded,
    onTrackClick: (PlayerScreenUiState.TrackUiModel) -> Unit,
    onRemoveTrackClick: (PlayerScreenUiState.TrackUiModel) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
    ) {
        items(uiState.tracks) { trackUiModel ->
            Track(
                trackUiModel = trackUiModel,
                onTrackClick = onTrackClick,
                onRemoveClick = onRemoveTrackClick,
            )
        }
    }
}

@Composable
private fun Track(
    trackUiModel: PlayerScreenUiState.TrackUiModel,
    onTrackClick: (PlayerScreenUiState.TrackUiModel) -> Unit,
    onRemoveClick: (PlayerScreenUiState.TrackUiModel) -> Unit,
) {
    val onTrackClickLambda = remember {
        {
            onTrackClick(trackUiModel)
        }
    }
    val onRemoveClickLambda = remember {
        {
            onRemoveClick(trackUiModel)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(TRACK_HEIGHT)
            .clickable(onClick = onTrackClickLambda)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp), // Should use something like MaterialTheme.padding.SizeM
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            val fontWeight = if (trackUiModel.isPlaying) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            }
            Text(
                text = trackUiModel.trackName,
                modifier = Modifier.weight(1f),
                fontWeight = fontWeight,
            )

            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete track",
                modifier = Modifier.clickable(
                    onClick = onRemoveClickLambda,
                )
            )
        }
        HorizontalDivider()
    }
}

@Preview(showBackground = true)
@Composable
private fun TrackPreview() {
    Column {
        Track(
            trackUiModel = PlayerScreenUiState.TrackUiModel(
                uid = 1,
                trackName = "Track name playing",
                isPlaying = true,
            ),
            onTrackClick = {},
            onRemoveClick = {},
        )
        Track(
            trackUiModel = PlayerScreenUiState.TrackUiModel(
                uid = 1,
                trackName = "Track name",
                isPlaying = false,
            ),
            onTrackClick = {},
            onRemoveClick = {},
        )
    }
}
