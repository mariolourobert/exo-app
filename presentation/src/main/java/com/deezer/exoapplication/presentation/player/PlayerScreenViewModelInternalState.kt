package com.deezer.exoapplication.presentation.player

import android.os.Parcelable
import com.deezer.exoapplication.domain.models.PlaylistDomainModel
import com.deezer.exoapplication.domain.models.TrackDomainModel
import kotlinx.parcelize.Parcelize

sealed interface PlayerScreenViewModelInternalState : Parcelable {
    @Parcelize
    data class Loaded(
        val playlist: PlaylistDomainModel,
        val tracks: List<TrackDomainModel>,
        val selectedTrack: TrackDomainModel? = null,
    ) : PlayerScreenViewModelInternalState

    @Parcelize
    data object InitialLoading : PlayerScreenViewModelInternalState

    @Parcelize
    data class Error(
        val playlistId: Int,
        val errorType: ErrorType,
    ) : PlayerScreenViewModelInternalState

    enum class ErrorType {
        PLAYLIST_NOT_FOUND,
        INVALID_PLAYLIST,
    }
}
