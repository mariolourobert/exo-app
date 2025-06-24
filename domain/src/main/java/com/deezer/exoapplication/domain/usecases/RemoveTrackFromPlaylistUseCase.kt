package com.deezer.exoapplication.domain.usecases

import com.deezer.exoapplication.utils.ResultOf

interface RemoveTrackFromPlaylistUseCase {
    suspend operator fun invoke(trackId: Int, playlistId: Int): ResultOf<Unit, Unit>
}
