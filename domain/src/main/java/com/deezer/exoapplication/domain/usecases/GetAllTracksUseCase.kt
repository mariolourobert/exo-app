package com.deezer.exoapplication.domain.usecases

import com.deezer.exoapplication.domain.models.TrackDomainModel

interface GetAllTracksUseCase {
    suspend operator fun invoke(): List<TrackDomainModel>
}
