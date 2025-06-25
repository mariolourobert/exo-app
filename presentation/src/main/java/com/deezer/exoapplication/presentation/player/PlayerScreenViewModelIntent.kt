package com.deezer.exoapplication.presentation.player

sealed interface PlayerScreenViewModelIntent {
    data class OnTrackClick(val trackId: Int) : PlayerScreenViewModelIntent
    data class OnRemoveTrackClick(val trackId: Int) : PlayerScreenViewModelIntent
    data object OnCurrentTrackFinished : PlayerScreenViewModelIntent
    data object OnAddTrackClick : PlayerScreenViewModelIntent
    data object OnLibraryDialogDismissRequest : PlayerScreenViewModelIntent
    data class OnNewTrackAddedToPlaylist(val trackId: Int) : PlayerScreenViewModelIntent
}
