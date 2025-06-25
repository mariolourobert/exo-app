package com.deezer.domain.usecases

import com.deezer.exoapplication.data.repositories.PlaylistRepository
import com.deezer.exoapplication.domain.usecases.RemoveTrackFromPlaylistUseCaseImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.random.Random

class RemoveTrackFromPlaylistUseCaseImplTest {
    private val mockPlaylistRepository = mockk<PlaylistRepository>()

    private val sut = RemoveTrackFromPlaylistUseCaseImpl(
        playlistRepository = mockPlaylistRepository,
    )

    /**
     * GIVEN:
     * - playlistRepository.removeTrackFromPlaylist returns true
     *
     * WHEN:
     * - invoke is called with a trackId and playlistId
     *
     * THEN:
     * - ResultOf.success(Unit) is returned
     */
    @Test
    fun `invoke returns ResultOf#success when track is removed from playlist`() = runTest {
        // GIVEN
        val trackId = Random.nextInt()
        val playlistId = Random.nextInt()
        coEvery {
            mockPlaylistRepository.removeTrackFromPlaylist(
                trackId = trackId,
                playlistId = playlistId,
            )
        } returns true

        // WHEN
        val result = sut(
            trackId = trackId,
            playlistId = playlistId,
        )

        // THEN
        assert(result.isSuccess)
        coVerify {
            mockPlaylistRepository.removeTrackFromPlaylist(
                trackId = trackId,
                playlistId = playlistId,
            )
        }
    }

    /**
     * GIVEN:
     * - playlistRepository.removeTrackFromPlaylist returns false
     *
     * WHEN:
     * - invoke is called with a trackId and playlistId
     *
     * THEN:
     * - ResultOf.failure(Unit) is returned
     */
    @Test
    fun `invoke returns ResultOf#failure when track removal fails`() = runTest {
        // GIVEN
        val trackId = Random.nextInt()
        val playlistId = Random.nextInt()
        coEvery {
            mockPlaylistRepository.removeTrackFromPlaylist(
                trackId = trackId,
                playlistId = playlistId,
            )
        } returns false

        // WHEN
        val result = sut(
            trackId = trackId,
            playlistId = playlistId,
        )

        // THEN
        assert(result.isFailure)
        coVerify {
            mockPlaylistRepository.removeTrackFromPlaylist(
                trackId = trackId,
                playlistId = playlistId,
            )
        }
    }
}
