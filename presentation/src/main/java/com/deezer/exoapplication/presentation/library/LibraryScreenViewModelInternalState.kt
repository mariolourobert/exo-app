package com.deezer.exoapplication.presentation.library

import android.os.Parcelable
import com.deezer.exoapplication.domain.models.TrackDomainModel
import kotlinx.parcelize.Parcelize

sealed interface LibraryScreenViewModelInternalState : Parcelable {
    @Parcelize
    data object InitialLoading : LibraryScreenViewModelInternalState

    @Parcelize
    data class Loaded(val tracks: List<TrackDomainModel>) : LibraryScreenViewModelInternalState
}
