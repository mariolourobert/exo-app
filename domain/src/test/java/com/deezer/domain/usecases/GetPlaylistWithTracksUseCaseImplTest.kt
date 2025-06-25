package com.deezer.domain.usecases

import app.cash.turbine.test
import com.deezer.data.models.nextPlaylistDataModel
import com.deezer.data.models.nextPlaylistWithTracksDataModel
import com.deezer.exoapplication.data.models.PlaylistWithTracksDataModel
import com.deezer.exoapplication.data.repositories.PlaylistRepository
import com.deezer.exoapplication.domain.mappers.PlaylistDomainModelMapper
import com.deezer.exoapplication.domain.mappers.TrackDomainModelMapper
import com.deezer.exoapplication.domain.usecases.GetPlaylistWithTracksUseCase.GetPlaylistWithTracksError
import com.deezer.exoapplication.domain.usecases.GetPlaylistWithTracksUseCaseImpl
import com.deezer.exoapplication.testUtils.TestDispatchersProvider
import com.deezer.exoapplication.utils.failureOrThrow
import com.deezer.exoapplication.utils.successOrThrow
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.random.Random
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class GetPlaylistWithTracksUseCaseImplTest {
    private val mockPlaylistRepository = mockk<PlaylistRepository>()
    private val playlistDomainModelMapper = PlaylistDomainModelMapper()
    private val trackDomainModelMapper = TrackDomainModelMapper()
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testDispatchersProvider = TestDispatchersProvider(testDispatcher)

    private val sut = GetPlaylistWithTracksUseCaseImpl(
        playlistRepository = mockPlaylistRepository,
        playlistDomainModelMapper = playlistDomainModelMapper,
        trackDomainModelMapper = trackDomainModelMapper,
        dispatchersProvider = testDispatchersProvider,
    )

    /**
     * GIVEN:
     * - playlistRepository.getPlaylistWithTracksAsFlow emits a valid PlaylistWithTracksDataModel
     *
     * WHEN:
     * - invoke is called with a playlistId
     *
     * THEN:
     * - returns a Flow that emits ResultOf.success with a PlaylistWithTracksDomainModel
     */
    @Test
    fun `invoke returns Flow with ResultOf#success containing PlaylistWithTracksDomainModel`() =
        runTest(testDispatcher) {
            // GIVEN
            val playlistId = Random.nextInt()
            val repositoryFlow = MutableSharedFlow<PlaylistWithTracksDataModel?>()
            coEvery {
                mockPlaylistRepository.getPlaylistWithTracksAsFlow(playlistId = playlistId)
            } returns repositoryFlow
            val validPlaylistWithTracksDataModel = Random.nextPlaylistWithTracksDataModel()

            sut(playlistId = playlistId).test {
                // WHEN
                repositoryFlow.emit(validPlaylistWithTracksDataModel)

                // THEN
                val result = awaitItem()
                assert(result.isSuccess)
                val playlistWithTracksDomainModel = result.successOrThrow()
                assertEquals(
                    expected = validPlaylistWithTracksDataModel.playlist.uid,
                    actual = playlistWithTracksDomainModel.playlist.uid,
                )
                assertEquals(
                    expected = validPlaylistWithTracksDataModel.playlist.playlistName,
                    actual = playlistWithTracksDomainModel.playlist.playlistName,
                )
                assertEquals(
                    expected = validPlaylistWithTracksDataModel.tracks.size,
                    actual = playlistWithTracksDomainModel.tracks.size,
                )
                cancelAndIgnoreRemainingEvents()
            }
            coVerify {
                mockPlaylistRepository.getPlaylistWithTracksAsFlow(playlistId = playlistId)
            }
        }

    /**
     * GIVEN:
     * - playlistRepository.getPlaylistWithTracksAsFlow emits a null PlaylistWithTracksDataModel
     *
     * WHEN:
     * - invoke is called with a playlistId
     *
     * THEN:
     * - returns a Flow that emits ResultOf.failure with GetPlaylistWithTracksError.PlaylistNotFound
     */
    @Test
    fun `invoke returns Flow with ResultOf#failure when PlaylistWithTracksDataModel is null`() =
        runTest(testDispatcher) {
            // GIVEN
            val playlistId = Random.nextInt()
            val repositoryFlow = MutableSharedFlow<PlaylistWithTracksDataModel?>()
            coEvery {
                mockPlaylistRepository.getPlaylistWithTracksAsFlow(playlistId = playlistId)
            } returns repositoryFlow

            sut(playlistId = playlistId).test {
                // WHEN
                repositoryFlow.emit(null)

                // THEN
                val result = awaitItem()
                assert(result.isFailure)
                assertEquals(
                    expected = GetPlaylistWithTracksError.PlaylistNotFound,
                    actual = result.failureOrThrow(),
                )
                cancelAndIgnoreRemainingEvents()
            }
        }

    /**
     * GIVEN:
     * - playlistRepository.getPlaylistWithTracksAsFlow emits a PlaylistWithTracksDataModel with an invalid playlist
     *
     * WHEN:
     * - invoke is called with a playlistId
     *
     * THEN:
     * - returns a Flow that emits ResultOf.failure with GetPlaylistWithTracksError.InvalidPlaylist
     */
    @Test
    fun `invoke returns Flow with ResultOf#failure when PlaylistWithTracksDataModel has invalid playlist`() =
        runTest(testDispatcher) {
            // GIVEN
            val playlistId = Random.nextInt()
            val repositoryFlow = MutableSharedFlow<PlaylistWithTracksDataModel?>()
            coEvery {
                mockPlaylistRepository.getPlaylistWithTracksAsFlow(playlistId = playlistId)
            } returns repositoryFlow
            val invalidPlaylistWithTracksDataModel = Random.nextPlaylistWithTracksDataModel(
                playlist = Random.nextPlaylistDataModel(
                    playlistName = null,
                ),
            )

            sut(playlistId = playlistId).test {
                // WHEN
                repositoryFlow.emit(invalidPlaylistWithTracksDataModel)

                // THEN
                val result = awaitItem()
                assert(result.isFailure)
                assertEquals(
                    expected = GetPlaylistWithTracksError.InvalidPlaylist,
                    actual = result.failureOrThrow(),
                )
                cancelAndIgnoreRemainingEvents()
            }
        }
}
