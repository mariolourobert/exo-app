package com.deezer.exoapplication.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaylistDomainModel(
    val uid: Int,
    val playlistName: String,
) : Parcelable
