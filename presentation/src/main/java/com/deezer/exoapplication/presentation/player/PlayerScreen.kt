package com.deezer.exoapplication.presentation.player

import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun PlayerScreen() {
    AndroidView(
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