package com.deezer.domain.usecases

import com.deezer.data.models.nextTrackDataModel
import com.deezer.exoapplication.data.repositories.TrackRepository
import com.deezer.exoapplication.domain.mappers.TrackDomainModelMapper
import com.deezer.exoapplication.domain.usecases.GetAllTracksUseCaseImpl
import com.deezer.exoapplication.testUtils.TestDispatchersProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.random.Random

@RunWith(RobolectricTestRunner::class)
class GetAllTracksUseCaseImplTest {
    private val mockTrackRepository = mockk<TrackRepository>()

    // We don't need to mock the TrackDomainModelMapper here
    private val trackDomainModelMapper = TrackDomainModelMapper()

    private val testDispatcher = StandardTestDispatcher()
    private val testDispatchersProvider = TestDispatchersProvider(testDispatcher)

    private val sut = GetAllTracksUseCaseImpl(
        trackRepository = mockTrackRepository,
        trackDomainModelMapper = trackDomainModelMapper,
        dispatchersProvider = testDispatchersProvider,
    )

    /**
     * GIVEN:
     * - mockTrackRepository returns a list of valid TrackDataModel
     *
     * WHEN:
     * - invoke is called
     *
     * THEN:
     * - returns a list of TrackDomainModel with the same size as the input list
     */
    @Test
    fun `invoke returns list of TrackDomainModel with same size as input list`() = runTest(testDispatcher) {
        // GIVEN
        val tracks = List(Random.nextInt(1, 10)) {
            Random.nextTrackDataModel()
        }
        coEvery {
            mockTrackRepository.getAllTracks()
        } returns tracks

        // WHEN
        val result = sut.invoke()

        // THEN
        assert(result.size == tracks.size)
    }

    /**
     * GIVEN:
     * - mockTrackRepository returns a list of valid and invalid TrackDataModel
     *
     * WHEN:
     * - invoke is called
     *
     * THEN:
     * - returns a list of TrackDomainModel with only valid TrackDataModel mapped
     */
    @Test
    fun `invoke returns list of TrackDomainModel with only valid TrackDataModel mapped`() = runTest(testDispatcher) {
        // GIVEN
        val validTracks = List(Random.nextInt(1, 10)) {
            Random.nextTrackDataModel()
        }
        val invalidTracks = List(Random.nextInt(1, 5)) {
            Random.nextTrackDataModel(trackName = null)
        }
        coEvery {
            mockTrackRepository.getAllTracks()
        } returns (validTracks + invalidTracks).shuffled()

        // WHEN
        val result = sut.invoke()

        // THEN
        assert(result.size == validTracks.size)
    }
}
