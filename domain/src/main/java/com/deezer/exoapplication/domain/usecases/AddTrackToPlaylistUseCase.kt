package com.deezer.exoapplication.domain.usecases

import com.deezer.exoapplication.utils.ResultOf

interface AddTrackToPlaylistUseCase {
    suspend operator fun invoke(
        trackId: Int,
        playlistId: Int,
    ): ResultOf<Unit, Error>

    enum class Error {
        TRACK_NOT_FOUND,
        PLAYLIST_NOT_FOUND,
        TRACK_ALREADY_IN_PLAYLIST,
        UNKNOWN_ERROR
    }
}