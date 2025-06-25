package com.deezer.exoapplication.domain.mappers

import com.deezer.exoapplication.data.models.PlaylistDataModel
import com.deezer.exoapplication.domain.models.PlaylistDomainModel
import com.deezer.exoapplication.utils.ErrorLogger

class PlaylistDomainModelMapper {
    fun toDomainModel(
        playlistDataModel: PlaylistDataModel,
    ): PlaylistDomainModel? {
        val playlistName = playlistDataModel.playlistName
            ?: run {
                ErrorLogger.logError(
                    "Playlist name is null for playlist with UID: ${playlistDataModel.uid}",
                )
                return null
            }

        return PlaylistDomainModel(
            uid = playlistDataModel.uid,
            playlistName = playlistName,
        )
    }
}
