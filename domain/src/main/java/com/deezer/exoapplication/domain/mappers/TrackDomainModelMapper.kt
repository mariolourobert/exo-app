package com.deezer.exoapplication.domain.mappers

import androidx.core.net.toUri
import com.deezer.exoapplication.data.models.TrackDataModel
import com.deezer.exoapplication.domain.models.TrackDomainModel

class TrackDomainModelMapper {
    fun toDomainModel(
        trackDataModel: TrackDataModel,
    ): TrackDomainModel? {
        val trackName = trackDataModel.trackName
        // TODO log error
            ?: return null

        val trackUri = trackDataModel.trackUri
            ?.let {
                try {
                    it.toUri()
                } catch (exception: NullPointerException) {
                    // TODO log error
                    null
                }
            }
            ?: return null

        return TrackDomainModel(
            uid = trackDataModel.uid,
            trackName = trackName,
            trackUri = trackUri,
        )
    }
}
