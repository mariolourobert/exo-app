package com.deezer.exoapplication.presentation.library

import kotlinx.collections.immutable.toImmutableList

class LibraryScreenUiStateMapper {
    fun toUiState(
        internalState: LibraryScreenViewModelInternalState,
    ): LibraryScreenUiState = when (internalState) {
        LibraryScreenViewModelInternalState.InitialLoading ->
            LibraryScreenUiState.Loading

        is LibraryScreenViewModelInternalState.Loaded ->
            mapLoadedState(internalState = internalState)
    }

    private fun mapLoadedState(
        internalState: LibraryScreenViewModelInternalState.Loaded,
    ): LibraryScreenUiState =
        if (internalState.tracks.isEmpty()) {
            LibraryScreenUiState.EmptyLibrary
        } else {
            LibraryScreenUiState.Loaded(
                tracks = internalState.tracks.map { track ->
                    LibraryScreenUiState.TrackUiModel(
                        uid = track.uid,
                        trackName = track.trackName,
                    )
                }.toImmutableList()
            )
        }
}
