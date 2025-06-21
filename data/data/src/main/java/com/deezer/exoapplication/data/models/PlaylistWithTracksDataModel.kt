package com.deezer.exoapplication.data.models

data class PlaylistWithTracksDataModel(
    val playlist: PlaylistDataModel,
    val tracks: List<TrackDataModel>,
)
