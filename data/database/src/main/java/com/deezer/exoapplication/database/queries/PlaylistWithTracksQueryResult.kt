package com.deezer.exoapplication.database.queries

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.deezer.exoapplication.database.entities.PlaylistEntity
import com.deezer.exoapplication.database.entities.PlaylistTrackCrossRef
import com.deezer.exoapplication.database.entities.TrackEntity

data class PlaylistWithTracksQueryResult(
    @Embedded
    val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "trackId",
        associateBy = Junction(PlaylistTrackCrossRef::class)
    )
    val tracks: List<TrackEntity>,
)
