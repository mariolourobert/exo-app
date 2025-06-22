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
                toLoadedState(internalState = internalState)

            PlayerScreenViewModelInternalState.InitialLoading ->
                PlayerScreenUiState.Loading

            is PlayerScreenViewModelInternalState.Error ->
                toErrorState(internalState = internalState)
        }

    private fun toErrorState(
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

    private fun toLoadedState(
        internalState: PlayerScreenViewModelInternalState.Loaded,
    ): PlayerScreenUiState.Loaded =
        PlayerScreenUiState.Loaded(
            tracks = internalState.tracks
                .map(::toUiModel)
                .toImmutableList(),
            currentPlayedTrackName = internalState.selectedTrack?.trackName,
        )

    private fun toUiModel(
        track: TrackDomainModel,
    ): TrackUiModel =
        TrackUiModel(
            uid = track.uid,
            trackName = track.trackName,
        )
}
