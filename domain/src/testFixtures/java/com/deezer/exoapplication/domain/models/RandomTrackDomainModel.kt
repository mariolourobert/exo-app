package com.deezer.exoapplication.domain.models

import android.net.Uri
import com.deezer.exoapplication.testUtils.nextString
import kotlin.random.Random

fun Random.nextTrackDomainModel(
    uid: Int = nextInt(),
    trackName: String = nextString(),
    trackUri: String = nextString(),
): TrackDomainModel =
    TrackDomainModel(
        uid = uid,
        trackName = trackName,
        trackUri = Uri.parse(trackUri),
    )
