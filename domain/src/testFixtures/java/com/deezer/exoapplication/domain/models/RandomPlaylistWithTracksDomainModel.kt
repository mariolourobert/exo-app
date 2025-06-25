package com.deezer.exoapplication.domain.models

import kotlin.random.Random

fun Random.nextPlaylistWithTracksDomainModel(
    playlist: PlaylistDomainModel = nextPlaylistDomainModel(),
    tracks: List<TrackDomainModel> = List(nextInt(1, 5)) { nextTrackDomainModel() },
): PlaylistWithTracksDomainModel = PlaylistWithTracksDomainModel(
    playlist = playlist,
    tracks = tracks,
)
