package com.deezer.exoapplication.database.debug

import com.deezer.exoapplication.database.daos.PlaylistDao
import com.deezer.exoapplication.database.daos.TrackDao
import com.deezer.exoapplication.database.entities.PlaylistEntity
import com.deezer.exoapplication.database.entities.TrackEntity

private const val DEFAULT_PLAYLIST_ID = 1

/**
 * Manager for populating the database with initial data.
 * It is just a hack for this exercise.
 * In a real project, it could be moved to a debug folder instead of main (at least)
 * to avoid shipping it to production.
 */
class PopulateDatabaseManager(
    private val playlistDao: PlaylistDao,
    private val trackDao: TrackDao,
) {
    suspend fun populate() {
        if (playlistDao.getPlaylist(DEFAULT_PLAYLIST_ID) == null) {
            playlistDao.insert(
                PlaylistEntity(
                    uid = DEFAULT_PLAYLIST_ID,
                    playlistName = "My Playlist",
                )
            )

            trackDao.insert(
                TrackEntity(
                    trackName = "Track 1",
                    trackUri = "https://filesamples.com/samples/audio/mp3/sample1.mp3",
                )
            )
            trackDao.insert(
                TrackEntity(
                    trackName = "Track 2",
                    trackUri = "https://filesamples.com/samples/audio/mp3/sample2.mp3",
                )
            )
            trackDao.insert(
                TrackEntity(
                    trackName = "Track 3",
                    trackUri = "https://filesamples.com/samples/audio/mp3/sample3.mp3",
                )
            )
            trackDao.insert(
                TrackEntity(
                    trackName = "Track 4",
                    trackUri = "https://filesamples.com/samples/audio/mp3/sample4.mp3",
                )
            )
            trackDao.insert(
                TrackEntity(
                    trackName = "Track 5",
                    trackUri = "https://filesamples.com/samples/audio/mp3/sample1.mp3",
                )
            )
            trackDao.insert(
                TrackEntity(
                    trackName = "Track 6",
                    trackUri = "https://filesamples.com/samples/audio/mp3/sample2.mp3",
                )
            )
        }
    }
}