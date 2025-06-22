package com.deezer.domain.mappers

import com.deezer.data.models.nextPlaylistDataModel
import com.deezer.exoapplication.domain.mappers.PlaylistDomainModelMapper
import org.junit.Test
import kotlin.random.Random
import kotlin.test.assertEquals

class PlaylistDomainModelMapperTest {
    private val sut = PlaylistDomainModelMapper()

    /**
     * GIVEN
     * - a PlaylistDataModel with valid playlistName
     *
     * WHEN
     * - toDomainModel is called
     *
     * THEN
     * - returns a PlaylistDomainModel with the same uid and playlistName
     */
    @Test
    fun `toDomainModel returns PlaylistDomainModel with valid playlistName`() {
        // GIVEN
        val playlistDataModel = Random.nextPlaylistDataModel()

        // WHEN
        val result = sut.toDomainModel(playlistDataModel)

        // THEN
        assert(result != null)
        assertEquals(
            expected = playlistDataModel.uid,
            actual = result!!.uid,
        )
        assertEquals(
            expected = playlistDataModel.playlistName,
            actual = result.playlistName,
        )
    }

    /**
     * GIVEN
     * - a PlaylistDataModel with null playlistName
     *
     * WHEN
     * - toDomainModel is called
     *
     * THEN
     * - returns null
     */
    @Test
    fun `toDomainModel returns null when playlistName is null`() {
        // GIVEN
        val playlistDataModel = Random.nextPlaylistDataModel(playlistName = null)

        // WHEN
        val result = sut.toDomainModel(playlistDataModel)

        // THEN
        assert(result == null)
    }
}
