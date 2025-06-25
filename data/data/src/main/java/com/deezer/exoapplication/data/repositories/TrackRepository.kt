package com.deezer.exoapplication.data.repositories

import com.deezer.exoapplication.data.models.TrackDataModel

interface TrackRepository {
    suspend fun getAllTracks(): List<TrackDataModel>
    suspend fun getTrackById(trackId: Int): TrackDataModel?
}
