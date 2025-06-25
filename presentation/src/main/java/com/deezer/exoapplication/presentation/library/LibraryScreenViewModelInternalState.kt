package com.deezer.exoapplication.presentation.library

import com.deezer.exoapplication.domain.models.TrackDomainModel

sealed interface LibraryScreenViewModelInternalState {
    data object InitialLoading : LibraryScreenViewModelInternalState
    data class Loaded(val tracks: List<TrackDomainModel>) : LibraryScreenViewModelInternalState
}
