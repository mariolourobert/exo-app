package com.deezer.domain.usecases

import com.deezer.data.models.nextPlaylistDataModel
import com.deezer.data.models.nextPlaylistWithTracksDataModel
import com.deezer.data.models.nextTrackDataModel
import com.deezer.exoapplication.data.repositories.PlaylistRepository
import com.deezer.exoapplication.data.repositories.TrackRepository
import com.deezer.exoapplication.domain.usecases.AddTrackToPlaylistUseCase
import com.deezer.exoapplication.domain.usecases.AddTrackToPlaylistUseCaseImpl
import com.deezer.exoapplication.utils.failureOrThrow
import com.deezer.exoapplication.utils.successOrThrow
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.random.Random
import kotlin.test.assertEquals

class AddTrackToPlaylistUseCaseImplTest {
    private val mockPlaylistRepository: PlaylistRepository = mockk()
    private val mockTrackRepository: TrackRepository = mockk()

    private val sut = AddTrackToPlaylistUseCaseImpl(
        playlistRepository = mockPlaylistRepository,
        trackRepository = mockTrackRepository,
    )

    /**
     * GIVEN:
     * - playlistRepository.getPlaylistWithTracks returns null
     *
     * WHEN:
     * - invoke is called with a trackId and playlistId
     *
     * THEN:
     * - ResultOf.Failure with Error.PLAYLIST_NOT_FOUND is returned
     */
    @Test
    fun `invoke returns PLAYLIST_NOT_FOUND when playlist is not found`() = runTest {
        // GIVEN
        val playlistId = Random.nextInt()
        coEvery {
            mockPlaylistRepository.getPlaylistWithTracks(playlistId = playlistId)
        } returns null

        // WHEN
        val result = sut(
            trackId = Random.nextInt(),
            playlistId = playlistId,
        )

        // THEN
        assert(result.isFailure)
        assertEquals(
            expected = AddTrackToPlaylistUseCase.Error.PLAYLIST_NOT_FOUND,
            actual = result.failureOrThrow(),
        )
        coVerify {
            mockPlaylistRepository.getPlaylistWithTracks(playlistId = playlistId)
        }
        coVerify(exactly = 0) {
            mockPlaylistRepository.addTrackToPlaylist(any(), any())
        }
    }

    /**
     * GIVEN:
     * - playlistRepository.getPlaylistWithTracks returns a playlist with tracks
     * - the track is already in the playlist
     *
     * WHEN:
     * - invoke is called with a trackId and playlistId
     *
     * THEN:
     * - ResultOf.Failure with Error.TRACK_ALREADY_IN_PLAYLIST is returned
     */
    @Test
    fun `invoke returns TRACK_ALREADY_IN_PLAYLIST when track is already in playlist`() = runTest {
        // GIVEN
        val playlistId = Random.nextInt()
        val trackId = Random.nextInt()
        val tracks = List(Random.nextInt(1, 10)) {
            Random.nextTrackDataModel()
        } + listOf(
            Random.nextTrackDataModel(
                uid = trackId,
            )
        )
        coEvery {
            mockPlaylistRepository.getPlaylistWithTracks(playlistId = playlistId)
        } returns Random.nextPlaylistWithTracksDataModel(
            tracks = tracks.shuffled(),
        )

        // WHEN
        val result = sut(
            trackId = trackId,
            playlistId = playlistId,
        )

        // THEN
        assert(result.isFailure)
        assertEquals(
            expected = AddTrackToPlaylistUseCase.Error.TRACK_ALREADY_IN_PLAYLIST,
            actual = result.failureOrThrow(),
        )
        coVerify {
            mockPlaylistRepository.getPlaylistWithTracks(playlistId = playlistId)
        }
        coVerify(exactly = 0) {
            mockPlaylistRepository.addTrackToPlaylist(any(), any())
        }
    }

    /**
     * GIVEN:
     * - playlistRepository.getPlaylistWithTracks returns a playlist with tracks
     * - trackRepository.getTrackById returns null
     *
     * WHEN:
     * - invoke is called with a trackId and playlistId
     *
     * THEN:
     * - ResultOf.Failure with Error.TRACK_NOT_FOUND is returned
     */
    @Test
    fun `invoke returns TRACK_NOT_FOUND when track is not found`() = runTest {
        // GIVEN
        val playlistId = Random.nextInt()
        val trackId = Random.nextInt()
        coEvery {
            mockPlaylistRepository.getPlaylistWithTracks(playlistId)
        } returns Random.nextPlaylistWithTracksDataModel()
        coEvery {
            mockTrackRepository.getTrackById(trackId)
        } returns null

        // WHEN
        val result = sut(
            trackId = trackId,
            playlistId = playlistId,
        )

        // THEN
        assert(result.isFailure)
        assertEquals(
            expected = AddTrackToPlaylistUseCase.Error.TRACK_NOT_FOUND,
            actual = result.failureOrThrow(),
        )
        coVerify {
            mockPlaylistRepository.getPlaylistWithTracks(playlistId = playlistId)
        }
        coVerify {
            mockTrackRepository.getTrackById(trackId = trackId)
        }
        coVerify(exactly = 0) {
            mockPlaylistRepository.addTrackToPlaylist(any(), any())
        }
    }

    /**
     * GIVEN:
     * - playlistRepository.getPlaylistWithTracks returns a playlist with tracks
     * - trackRepository.getTrackById returns a track
     * - playlistRepository.addTrackToPlaylist returns false
     *
     * WHEN:
     * - invoke is called with a trackId and playlistId
     *
     * THEN:
     * - ResultOf.Failure with Error.UNKNOWN_ERROR is returned
     */
    @Test
    fun `invoke returns UNKNOWN_ERROR when adding track to playlist fails`() = runTest {
        // GIVEN
        val playlistId = Random.nextInt()
        val trackId = Random.nextInt()
        val playlistDataModel = Random.nextPlaylistDataModel(
            uid = playlistId,
        )
        val track = Random.nextTrackDataModel(uid = trackId)
        coEvery {
            mockPlaylistRepository.getPlaylistWithTracks(playlistId)
        } returns Random.nextPlaylistWithTracksDataModel(
            playlist = playlistDataModel,
        )
        coEvery {
            mockTrackRepository.getTrackById(trackId)
        } returns track
        coEvery {
            mockPlaylistRepository.addTrackToPlaylist(trackId, playlistId)
        } returns false

        // WHEN
        val result = sut(
            trackId = trackId,
            playlistId = playlistId,
        )

        // THEN
        assert(result.isFailure)
        assertEquals(
            expected = AddTrackToPlaylistUseCase.Error.UNKNOWN_ERROR,
            actual = result.failureOrThrow(),
        )
        coVerify {
            mockPlaylistRepository.getPlaylistWithTracks(playlistId = playlistId)
        }
        coVerify {
            mockTrackRepository.getTrackById(trackId = trackId)
        }
        coVerify {
            mockPlaylistRepository.addTrackToPlaylist(trackId = trackId, playlistId = playlistId)
        }
    }

    /**
     * GIVEN:
     * - playlistRepository.getPlaylistWithTracks returns a playlist with tracks
     * - trackRepository.getTrackById returns a track
     * - playlistRepository.addTrackToPlaylist returns true
     *
     * WHEN:
     * - invoke is called with a trackId and playlistId
     *
     * THEN:
     * - ResultOf.Success(Unit) is returned
     */
    @Test
    fun `invoke returns Success when track is added to playlist successfully`() = runTest {
        // GIVEN
        val playlistId = Random.nextInt()
        val trackId = Random.nextInt()
        val playlistDataModel = Random.nextPlaylistDataModel(
            uid = playlistId,
        )
        val track = Random.nextTrackDataModel(uid = trackId)
        coEvery {
            mockPlaylistRepository.getPlaylistWithTracks(playlistId)
        } returns Random.nextPlaylistWithTracksDataModel(
            playlist = playlistDataModel,
        )
        coEvery {
            mockTrackRepository.getTrackById(trackId)
        } returns track
        coEvery {
            mockPlaylistRepository.addTrackToPlaylist(trackId, playlistId)
        } returns true

        // WHEN
        val result = sut(
            trackId = trackId,
            playlistId = playlistId,
        )

        // THEN
        assert(result.isSuccess)
        assertEquals(
            expected = Unit,
            actual = result.successOrThrow(),
        )
        coVerify {
            mockPlaylistRepository.getPlaylistWithTracks(playlistId = playlistId)
        }
        coVerify {
            mockTrackRepository.getTrackById(trackId = trackId)
        }
        coVerify {
            mockPlaylistRepository.addTrackToPlaylist(trackId = trackId, playlistId = playlistId)
        }
    }
}
