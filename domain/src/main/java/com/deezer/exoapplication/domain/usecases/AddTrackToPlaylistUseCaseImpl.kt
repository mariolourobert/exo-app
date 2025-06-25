package com.deezer.exoapplication.domain.usecases

import com.deezer.exoapplication.data.repositories.PlaylistRepository
import com.deezer.exoapplication.data.repositories.TrackRepository
import com.deezer.exoapplication.domain.usecases.AddTrackToPlaylistUseCase.Error
import com.deezer.exoapplication.utils.ResultOf

internal class AddTrackToPlaylistUseCaseImpl(
    private val playlistRepository: PlaylistRepository,
    private val trackRepository: TrackRepository,
) : AddTrackToPlaylistUseCase {
    override suspend fun invoke(
        trackId: Int,
        playlistId: Int,
    ): ResultOf<Unit, Error> {
        val playlistWithTracks = playlistRepository.getPlaylistWithTracks(
            playlistId = playlistId,
        )

        if (playlistWithTracks == null) {
            return ResultOf.Failure(Error.PLAYLIST_NOT_FOUND)
        }

        val trackAlreadyInPlaylist = playlistWithTracks.tracks.any { it.uid == trackId }

        if (trackAlreadyInPlaylist) {
            return ResultOf.Failure(Error.TRACK_ALREADY_IN_PLAYLIST)
        }

        val track = trackRepository.getTrackById(
            trackId = trackId,
        ) ?: return ResultOf.Failure(Error.TRACK_NOT_FOUND)

        val success = playlistRepository.addTrackToPlaylist(
            trackId = track.uid,
            playlistId = playlistWithTracks.playlist.uid,
        )

        return if (success) {
            ResultOf.Success(Unit)
        } else {
            ResultOf.Failure(Error.UNKNOWN_ERROR)
        }
    }
}
