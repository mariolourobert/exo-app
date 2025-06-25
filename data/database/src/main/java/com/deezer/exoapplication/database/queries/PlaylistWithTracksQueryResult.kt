package com.deezer.exoapplication.database.queries

import androidx.room.Embedded
import com.deezer.exoapplication.database.entities.TrackEntity

data class PlaylistWithTracksQueryResult(
    @Embedded
    val track: TrackEntity,
)
