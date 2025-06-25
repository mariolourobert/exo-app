package com.deezer.exoapplication.domain.models

import com.deezer.exoapplication.testUtils.nextString
import kotlin.random.Random

fun Random.nextPlaylistDomainModel(
    uid: Int = nextInt(),
    playlistName: String = nextString(),
): PlaylistDomainModel =
    PlaylistDomainModel(
        uid = uid,
        playlistName = playlistName,
)
