package com.deezer.exoapplication.data.mappers

import com.deezer.exoapplication.data.models.PlaylistDataModel
import com.deezer.exoapplication.database.entities.PlaylistEntity

internal class PlaylistDataModelMapper {
    fun toDataModel(
        playlistEntity: PlaylistEntity
    ): PlaylistDataModel? =
        playlistEntity.uid?.let { uid ->
            PlaylistDataModel(
                uid = uid,
                playlistName = playlistEntity.playlistName
            )
        }
}
