package com.deezer.exoapplication.domain.models

data class PlaylistWithTracksDomainModel(
    val playlist: PlaylistDomainModel,
    val tracks: List<TrackDomainModel>,
)
