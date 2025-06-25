package com.deezer.exoapplication.domain.mappers

import androidx.core.net.toUri
import com.deezer.exoapplication.data.models.TrackDataModel
import com.deezer.exoapplication.domain.models.TrackDomainModel
import com.deezer.exoapplication.utils.ErrorLogger

class TrackDomainModelMapper {
    fun toDomainModel(
        trackDataModel: TrackDataModel,
    ): TrackDomainModel? {
        val trackName = trackDataModel.trackName
            ?: run {
                ErrorLogger.logError(
                    "Track name is null for track with UID: ${trackDataModel.uid}",
                )
                return null
            }

        val trackUri = trackDataModel.trackUri
            ?.let {
                try {
                    it.toUri()
                } catch (exception: NullPointerException) {
                    ErrorLogger.logError(
                        "Track URI is null or invalid for track with UID: ${trackDataModel.uid}. Exception: ${exception.message}",
                    )
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
