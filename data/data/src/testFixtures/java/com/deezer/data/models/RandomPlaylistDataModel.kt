package com.deezer.data.models

import com.deezer.exoapplication.data.models.PlaylistDataModel
import com.deezer.exoapplication.testUtils.nextString
import kotlin.random.Random

fun Random.nextPlaylistDataModel(
    uid: Int = nextInt(0, Int.MAX_VALUE),
    playlistName: String? = nextString(),
): PlaylistDataModel =
    PlaylistDataModel(
        uid = uid,
        playlistName = playlistName,
)
