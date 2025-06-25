package com.deezer.exoapplication.presentation.player

import android.net.Uri
import com.deezer.exoapplication.presentation.utils.TextResource

sealed interface PlayerScreenEvent {
    data class PlayTrack(
        val trackUri: Uri,
    ) : PlayerScreenEvent
    data object StopAndUnloadAllTracks : PlayerScreenEvent
    data class ShowError(
        val message: TextResource,
    ) : PlayerScreenEvent
}
