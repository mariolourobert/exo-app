package com.deezer.exoapplication.data.repositories

import com.deezer.exoapplication.data.mappers.TrackDataModelMapper
import com.deezer.exoapplication.data.models.TrackDataModel
import com.deezer.exoapplication.database.daos.TrackDao

internal class TrackRepositoryImpl(
    private val trackDao: TrackDao,
    private val trackDataModelMapper: TrackDataModelMapper,
) : TrackRepository {
    override suspend fun getAllTracks(): List<TrackDataModel> =
        trackDao.getAllTracks().mapNotNull { trackEntity ->
            trackDataModelMapper.toDataModel(trackEntity)
        }
}
