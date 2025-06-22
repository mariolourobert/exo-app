package com.deezer.domain.mappers

import com.deezer.data.models.nextTrackDataModel
import com.deezer.exoapplication.domain.mappers.TrackDomainModelMapper
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class TrackDomainModelMapperTest {
    private val sut = TrackDomainModelMapper()

    /**
     * GIVEN
     * - a TrackDataModel with valid trackName and trackUri
     *
     * WHEN
     * - toDomainModel is called
     *
     * THEN
     * - returns a TrackDomainModel with the same uid, trackName, and trackUri
     */
    @Test
    fun `toDomainModel returns TrackDomainModel with valid trackName and trackUri`() {
        // GIVEN
        val trackDataModel = Random.nextTrackDataModel()

        // WHEN
        val result = sut.toDomainModel(trackDataModel)

        // THEN
        assert(result != null)
        assertEquals(
            expected = trackDataModel.uid,
            actual = result!!.uid,
        )
        assertEquals(
            expected = trackDataModel.trackName,
            actual = result.trackName,
        )
        assertEquals(
            expected = trackDataModel.trackUri,
            actual = result.trackUri.toString(),
        )
    }

    /**
     * GIVEN
     * - a TrackDataModel with null trackName
     *
     * WHEN
     * - toDomainModel is called
     *
     * THEN
     * - returns null
     */
    @Test
    fun `toDomainModel returns null when trackName is null`() {
        // GIVEN
        val trackDataModel = Random.nextTrackDataModel(trackName = null)

        // WHEN
        val result = sut.toDomainModel(trackDataModel)

        // THEN
        assert(result == null)
    }

    /**
     * GIVEN
     * - a TrackDataModel with null trackUri
     *
     * WHEN
     * - toDomainModel is called
     *
     * THEN
     * - returns null
     */
    @Test
    fun `toDomainModel returns null when trackUri is null`() {
        // GIVEN
        val trackDataModel = Random.nextTrackDataModel(trackUri = null)

        // WHEN
        val result = sut.toDomainModel(trackDataModel)

        // THEN
        assert(result == null)
    }
}
