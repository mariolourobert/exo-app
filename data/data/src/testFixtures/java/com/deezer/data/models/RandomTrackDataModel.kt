package com.deezer.data.models

import com.deezer.exoapplication.data.models.TrackDataModel
import com.deezer.exoapplication.testUtils.nextString
import kotlin.random.Random

fun Random.nextTrackDataModel(
    uid: Int = nextInt(0, Int.MAX_VALUE),
    trackName: String? = nextString(),
    trackUri: String? = nextString(),
): TrackDataModel =
    TrackDataModel(
        uid = uid,
        trackName = trackName,
        trackUri = trackUri,
)
