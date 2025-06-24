package com.deezer.exoapplication.presentation.player

import com.deezer.exoapplication.presentation.utils.TextResource
import kotlinx.collections.immutable.ImmutableList

sealed interface PlayerScreenUiState {
    data class Loaded(
        val tracks: ImmutableList<TrackUiModel>,
        val currentPlayedTrackName: String?,
    ) : PlayerScreenUiState

    data object Loading : PlayerScreenUiState

    data class Error(
        val message: TextResource,
    ) : PlayerScreenUiState

    data class TrackUiModel(
        val uid: Int,
        val trackName: String,
        val isPlaying: Boolean,
    )
}
