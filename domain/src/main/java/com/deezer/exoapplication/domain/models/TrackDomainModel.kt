package com.deezer.exoapplication.domain.models

import android.net.Uri

data class TrackDomainModel(
    val uid: Int,
    val trackName: String,
    val trackUri: Uri,
)
