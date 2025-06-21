package com.deezer.exoapplication.data.mappers

import com.deezer.exoapplication.data.models.TrackDataModel
import com.deezer.exoapplication.database.entities.TrackEntity

internal class TrackDataModelMapper {
    fun toDataModel(
        trackEntity: TrackEntity,
    ): TrackDataModel? =
        trackEntity.uid?.let { uid ->
            TrackDataModel(
                uid = uid,
                trackName = trackEntity.trackName,
                trackUri = trackEntity.trackUri,
            )
        }
}
