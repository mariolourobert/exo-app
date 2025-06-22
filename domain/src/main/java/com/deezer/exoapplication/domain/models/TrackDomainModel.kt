package com.deezer.exoapplication.domain.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrackDomainModel(
    val uid: Int,
    val trackName: String,
    val trackUri: Uri,
): Parcelable
