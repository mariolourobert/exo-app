package com.deezer.exoapplication.domain.usecases

import com.deezer.exoapplication.data.repositories.PlaylistRepository
import com.deezer.exoapplication.utils.ResultOf

internal class RemoveTrackFromPlaylistUseCaseImpl(
    private val playlistRepository: PlaylistRepository,
) : RemoveTrackFromPlaylistUseCase {
    override suspend fun invoke(trackId: Int, playlistId: Int): ResultOf<Unit, Unit> =
        playlistRepository.removeTrackFromPlaylist(
            trackId = trackId,
            playlistId = playlistId,
        ).let { success ->
            if (success) {
                ResultOf.success(Unit)
            } else {
                ResultOf.failure(Unit)
            }
        }
}
