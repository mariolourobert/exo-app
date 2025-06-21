package com.deezer.exoapplication.domain.mappers

import com.deezer.exoapplication.data.models.PlaylistDataModel
import com.deezer.exoapplication.domain.models.PlaylistDomainModel

class PlaylistDomainModelMapper {
    fun toDomainModel(
        playlistDataModel: PlaylistDataModel,
    ): PlaylistDomainModel? {
        val playlistName = playlistDataModel.playlistName
        // TODO log error
            ?: return null

        return PlaylistDomainModel(
            uid = playlistDataModel.uid,
            playlistName = playlistName,
        )
    }
}
