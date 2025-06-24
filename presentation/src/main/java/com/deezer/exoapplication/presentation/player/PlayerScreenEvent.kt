package com.deezer.exoapplication.presentation.player

import android.net.Uri

sealed interface PlayerScreenEvent {
    data class PlayTrack(
        val trackUri: Uri,
    ) : PlayerScreenEvent
}
