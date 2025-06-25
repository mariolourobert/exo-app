package com.deezer.exoapplication.presentation.player

import com.deezer.exoapplication.presentation.utils.TextResource
import kotlinx.collections.immutable.ImmutableList

sealed interface PlayerScreenUiState {
    val isLibraryDialogVisible: Boolean

    data class Loaded(
        val tracks: ImmutableList<TrackUiModel>,
        val currentPlayedTrackName: String?,
        override val isLibraryDialogVisible: Boolean,
    ) : PlayerScreenUiState

    data class EmptyPlaylist(
        override val isLibraryDialogVisible: Boolean,
    ) : PlayerScreenUiState {
    }

    data object Loading : PlayerScreenUiState {
        override val isLibraryDialogVisible: Boolean
            get() = false
    }

    data class Error(
        val message: TextResource,
    ) : PlayerScreenUiState {
        override val isLibraryDialogVisible: Boolean
            get() = false
    }

    data class TrackUiModel(
        val uid: Int,
        val trackName: String,
        val isPlaying: Boolean,
    )
}
