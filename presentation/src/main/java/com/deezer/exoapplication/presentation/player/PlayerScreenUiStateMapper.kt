package com.deezer.exoapplication.presentation.player

import com.deezer.exoapplication.domain.models.TrackDomainModel
import com.deezer.exoapplication.presentation.R
import com.deezer.exoapplication.presentation.player.PlayerScreenUiState.TrackUiModel
import com.deezer.exoapplication.presentation.utils.TextResource
import kotlinx.collections.immutable.toImmutableList

class PlayerScreenUiStateMapper {
    fun toUiState(
        internalState: PlayerScreenViewModelInternalState,
    ): PlayerScreenUiState =
        when (internalState) {
            is PlayerScreenViewModelInternalState.Loaded ->
                mapLoadedState(internalState = internalState)

            PlayerScreenViewModelInternalState.InitialLoading ->
                PlayerScreenUiState.Loading

            is PlayerScreenViewModelInternalState.Error ->
                mapErrorState(internalState = internalState)
        }

    private fun mapErrorState(
        internalState: PlayerScreenViewModelInternalState.Error,
    ): PlayerScreenUiState.Error =
        PlayerScreenUiState.Error(
            message = when (internalState.errorType) {
                PlayerScreenViewModelInternalState.ErrorType.PLAYLIST_NOT_FOUND ->
                    TextResource.FromStringResource(R.string.playerscreen_error_playlist_not_found)

                PlayerScreenViewModelInternalState.ErrorType.INVALID_PLAYLIST ->
                    TextResource.FromStringResource(R.string.playerscreen_error_invalid_playlist)
            },
        )

    private fun mapLoadedState(
        internalState: PlayerScreenViewModelInternalState.Loaded,
    ): PlayerScreenUiState =
        if (internalState.tracks.isEmpty()) {
            PlayerScreenUiState.EmptyPlaylist(
                isLibraryDialogVisible = internalState.isLibraryDialogVisible,
            )
        } else {
            PlayerScreenUiState.Loaded(
                tracks = internalState.tracks
                    .map {
                        toUiModel(
                            track = it,
                            selectedTrack = internalState.selectedTrack,
                        )
                    }
                    .toImmutableList(),
                currentPlayedTrackName = internalState.selectedTrack?.trackName,
                isLibraryDialogVisible = internalState.isLibraryDialogVisible,
            )
        }

    private fun toUiModel(
        track: TrackDomainModel,
        selectedTrack: TrackDomainModel?,
    ): TrackUiModel =
        TrackUiModel(
            uid = track.uid,
            trackName = track.trackName,
            isPlaying = track == selectedTrack,
        )
}
