package com.deezer.presentation.player

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.deezer.exoapplication.domain.models.nextPlaylistDomainModel
import com.deezer.exoapplication.domain.models.nextPlaylistWithTracksDomainModel
import com.deezer.exoapplication.domain.models.nextTrackDomainModel
import com.deezer.exoapplication.domain.usecases.AddTrackToPlaylistUseCase
import com.deezer.exoapplication.domain.usecases.GetPlaylistWithTracksUseCase
import com.deezer.exoapplication.domain.usecases.GetPlaylistWithTracksUseCase.GetPlaylistWithTracksError
import com.deezer.exoapplication.domain.usecases.RemoveTrackFromPlaylistUseCase
import com.deezer.exoapplication.presentation.player.PlayerScreenEvent
import com.deezer.exoapplication.presentation.player.PlayerScreenUiState
import com.deezer.exoapplication.presentation.player.PlayerScreenUiStateMapper
import com.deezer.exoapplication.presentation.player.PlayerScreenViewModel
import com.deezer.exoapplication.presentation.player.PlayerScreenViewModelIntent
import com.deezer.exoapplication.testUtils.TestDispatchersProvider
import com.deezer.exoapplication.testUtils.nextString
import com.deezer.exoapplication.utils.ResultOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.random.Random
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class PlayerScreenViewModelTest {
    private val mockGetPlaylistWithTracksUseCase = mockk<GetPlaylistWithTracksUseCase>()
    private val mockRemoveTrackFromPlaylistUseCase = mockk<RemoveTrackFromPlaylistUseCase>()
    private val mockAddTrackToPlaylistUseCase = mockk<AddTrackToPlaylistUseCase>()
    private val playerScreenUiStateMapper = PlayerScreenUiStateMapper()
    private val savedStateHandle = SavedStateHandle()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testDispatchersProvider = TestDispatchersProvider(testDispatcher)

    private val sut = PlayerScreenViewModel(
        getPlaylistWithTracksUseCase = mockGetPlaylistWithTracksUseCase,
        removeTrackFromPlaylistUseCase = mockRemoveTrackFromPlaylistUseCase,
        addTrackToPlaylistUseCase = mockAddTrackToPlaylistUseCase,
        playerScreenUiStateMapper = playerScreenUiStateMapper,
        savedStateHandle = savedStateHandle,
        dispatchersProvider = testDispatchersProvider,
    )

    /**
     * GIVEN:
     * - sut is just created
     *
     * WHEN:
     * - sut.fetchDataIfNecessary is called
     *
     * THEN:
     * - getPlaylistWithTracksUseCase is called with the correct playlistId
     *
     * WHEN:
     * - sut.fetchDataIfNecessary is called again with same playlistId
     *
     * THEN:
     * - getPlaylistWithTracksUseCase is not called again
     */
    @Test
    fun `test fetchDataIfNecessary calls getPlaylistWithTracksUseCase with correct playlistId`() =
        runTest(testDispatcher) {
            // GIVEN
            val playlistId = Random.nextInt()
            coEvery {
                mockGetPlaylistWithTracksUseCase(playlistId = playlistId)
            } returns flowOf(
                ResultOf.success(
                    Random.nextPlaylistWithTracksDomainModel(
                        playlist = Random.nextPlaylistDomainModel(
                            uid = playlistId,
                        )
                    ),
                ),
            )

            // WHEN
            sut.fetchDataIfNecessary(playlistId = playlistId)

            // THEN
            coVerify {
                mockGetPlaylistWithTracksUseCase(playlistId = playlistId)
            }

            // WHEN
            sut.fetchDataIfNecessary(playlistId = playlistId)

            // THEN
            coVerify(exactly = 1) {
                mockGetPlaylistWithTracksUseCase(playlistId = playlistId)
            }
        }

    /**
     * GIVEN:
     * - getPlaylistWithTracksUseCase emits a ResultOf.success with a PlaylistWithTracksDomainModel
     *
     * WHEN:
     * - sut.fetchDataIfNecessary is called
     *
     * THEN:
     * - sut.uiState is updated with the correct PlaylistWithTracksUiState
     */
    @Test
    fun `test fetchDataIfNecessary updates uiState with correct PlaylistWithTracksUiState`() =
        runTest(testDispatcher) {
            // GIVEN
            val playlistId = Random.nextInt()
            val playlistWithTracksDomainModel = Random.nextPlaylistWithTracksDomainModel(
                playlist = Random.nextPlaylistDomainModel(
                    uid = playlistId,
                ),
            )
            coEvery {
                mockGetPlaylistWithTracksUseCase(playlistId = playlistId)
            } returns flowOf(ResultOf.success(playlistWithTracksDomainModel))

            sut.uiState.test {
                // WHEN
                sut.fetchDataIfNecessary(playlistId = playlistId)

                // Skip the initial loading state
                awaitItem()

                // THEN
                val uiState = awaitItem()
                assert(uiState is PlayerScreenUiState.Loaded)
                uiState as PlayerScreenUiState.Loaded
                assertEquals(
                    expected = playlistWithTracksDomainModel.tracks.size,
                    actual = uiState.tracks.size,
                )
            }
        }

    /**
     * GIVEN:
     * - getPlaylistWithTracksUseCase emits a ResultOf.success without tracks
     *
     * WHEN:
     * - sut.fetchDataIfNecessary is called
     *
     * THEN:
     * - sut.uiState is updated with an Empty State
     */
    @Test
    fun `test fetchDataIfNecessary updates uiState with Empty State when no tracks are found`() =
        runTest(testDispatcher) {
            // GIVEN
            val playlistId = Random.nextInt()
            coEvery {
                mockGetPlaylistWithTracksUseCase(playlistId = playlistId)
            } returns flowOf(ResultOf.success(Random.nextPlaylistWithTracksDomainModel(tracks = emptyList())))

            sut.uiState.test {
                // WHEN
                sut.fetchDataIfNecessary(playlistId = playlistId)

                // Skip the initial loading state
                awaitItem()

                // THEN
                val uiState = awaitItem()
                assert(uiState is PlayerScreenUiState.EmptyPlaylist)
            }
        }

    /**
     * GIVEN:
     * - getPlaylistWithTracksUseCase emits a ResultOf.failure with PlaylistNotFound
     *
     * WHEN:
     * - sut.fetchDataIfNecessary is called
     *
     * THEN:
     * - sut.uiState is updated with an Error State
     */
    @Test
    fun `test fetchDataIfNecessary updates uiState with Error State when playlist is not found`() =
        runTest(testDispatcher) {
            // GIVEN
            val playlistId = Random.nextInt()
            coEvery {
                mockGetPlaylistWithTracksUseCase(playlistId = playlistId)
            } returns flowOf(
                ResultOf.failure(
                    value = GetPlaylistWithTracksError.PlaylistNotFound,
                ),
            )

            sut.uiState.test {
                // WHEN
                sut.fetchDataIfNecessary(playlistId = playlistId)

                // Skip the initial loading state
                awaitItem()

                // THEN
                val uiState = awaitItem()
                assert(uiState is PlayerScreenUiState.Error)
            }
        }

    /**
     * GIVEN:
     * - getPlaylistWithTracksUseCase emits a ResultOf.failure with InvalidPlaylist
     *
     * WHEN:
     * - sut.fetchDataIfNecessary is called
     *
     * THEN:
     * - sut.uiState is updated with an Error State
     */
    @Test
    fun `test fetchDataIfNecessary updates uiState with Error State when playlist is invalid`() =
        runTest(testDispatcher) {
            // GIVEN
            val playlistId = Random.nextInt()
            coEvery {
                mockGetPlaylistWithTracksUseCase(playlistId = playlistId)
            } returns flowOf(
                ResultOf.failure(
                    value = GetPlaylistWithTracksError.InvalidPlaylist,
                ),
            )

            sut.uiState.test {
                // WHEN
                sut.fetchDataIfNecessary(playlistId = playlistId)

                // Skip the initial loading state
                awaitItem()

                // THEN
                val uiState = awaitItem()
                assert(uiState is PlayerScreenUiState.Error)
            }
        }

    /**
     * GIVEN:
     * - the sut is Loaded with tracks
     *
     * WHEN:
     * - sut.onIntent(OnTrackClick) is called with a track in the playlist
     *
     * THEN:
     * - a PlayTrack event is emitted with the selected track uri
     */
    @Test
    fun `test onIntent OnTrackClick emits PlayTrack event`() =
        runTest(testDispatcher) {
            // GIVEN
            val playlistId = Random.nextInt()
            val selectedTrackId = Random.nextInt()
            val selectedTrackName = Random.nextString()
            val selectedTrackUri = Random.nextString()
            val playlistWithTracksDomainModel = Random.nextPlaylistWithTracksDomainModel(
                playlist = Random.nextPlaylistDomainModel(
                    uid = playlistId,
                ),
                tracks = (List(
                    size = Random.nextInt(1, 5),
                ) {
                    Random.nextTrackDomainModel()
                } + Random.nextTrackDomainModel(
                    uid = selectedTrackId,
                    trackName = selectedTrackName,
                    trackUri = selectedTrackUri,
                )).shuffled(),
            )
            coEvery {
                mockGetPlaylistWithTracksUseCase(playlistId = playlistId)
            } returns flowOf(ResultOf.success(playlistWithTracksDomainModel))

            sut.event.test {
                sut.fetchDataIfNecessary(playlistId = playlistId)

                // WHEN
                sut.onIntent(PlayerScreenViewModelIntent.OnTrackClick(trackId = selectedTrackId))

                // THEN
                val event = awaitItem()
                assert(event is PlayerScreenEvent.PlayTrack)
                assertEquals(
                    expected = selectedTrackUri,
                    actual = (event as PlayerScreenEvent.PlayTrack).trackUri.toString(),
                )
            }
        }

    // More tests can be added here for other intents like OnAddTrackClick, OnRemoveTrackClick, etc.
}