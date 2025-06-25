package com.deezer.data.models

import com.deezer.exoapplication.data.models.PlaylistDataModel
import com.deezer.exoapplication.data.models.PlaylistWithTracksDataModel
import com.deezer.exoapplication.data.models.TrackDataModel
import kotlin.random.Random

fun Random.nextPlaylistWithTracksDataModel(
    playlist: PlaylistDataModel = nextPlaylistDataModel(),
    tracks: List<TrackDataModel> = List(nextInt(1, 10)) { nextTrackDataModel() },
): PlaylistWithTracksDataModel =
    PlaylistWithTracksDataModel(
        playlist = playlist,
        tracks = tracks,
    )
