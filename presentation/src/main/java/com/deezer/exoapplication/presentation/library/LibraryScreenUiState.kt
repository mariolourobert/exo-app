package com.deezer.exoapplication.presentation.library

import kotlinx.collections.immutable.ImmutableList

sealed interface LibraryScreenUiState {
    data object Loading : LibraryScreenUiState
    data object EmptyLibrary : LibraryScreenUiState
    data class Loaded(val tracks: ImmutableList<TrackUiModel>) : LibraryScreenUiState

    data class TrackUiModel(
        val uid: Int,
        val trackName: String,
    )
}
