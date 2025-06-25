package com.deezer.exoapplication.presentation.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deezer.exoapplication.domain.models.PlaylistWithTracksDomainModel
import com.deezer.exoapplication.domain.models.TrackDomainModel
import com.deezer.exoapplication.domain.usecases.AddTrackToPlaylistUseCase
import com.deezer.exoapplication.domain.usecases.GetPlaylistWithTracksUseCase
import com.deezer.exoapplication.domain.usecases.GetPlaylistWithTracksUseCase.GetPlaylistWithTracksError
import com.deezer.exoapplication.domain.usecases.RemoveTrackFromPlaylistUseCase
import com.deezer.exoapplication.presentation.R
import com.deezer.exoapplication.presentation.utils.TextResource
import com.deezer.exoapplication.utils.DispatchersProvider
import com.deezer.exoapplication.utils.ErrorLogger
import com.deezer.exoapplication.utils.ResultOf
import com.deezer.exoapplication.utils.mapStateFlow
import com.deezer.exoapplication.utils.onFailure
import com.deezer.exoapplication.utils.onSuccess
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlayerScreenViewModel(
    private val getPlaylistWithTracksUseCase: GetPlaylistWithTracksUseCase,
    private val removeTrackFromPlaylistUseCase: RemoveTrackFromPlaylistUseCase,
    private val addTrackToPlaylistUseCase: AddTrackToPlaylistUseCase,
    private val playerScreenUiStateMapper: PlayerScreenUiStateMapper,
    private val savedStateHandle: SavedStateHandle,
    private val dispatchersProvider: DispatchersProvider,
) : ViewModel() {
    companion object {
        private const val INTERNAL_STATE_KEY = "internal_state_key"
    }

    val uiState: StateFlow<PlayerScreenUiState> =
        savedStateHandle.getStateFlow<PlayerScreenViewModelInternalState>(
            key = INTERNAL_STATE_KEY,
            initialValue = PlayerScreenViewModelInternalState.InitialLoading,
        ).mapStateFlow(
            coroutineScope = viewModelScope,
            transform = playerScreenUiStateMapper::toUiState,
        )

    val event: SharedFlow<PlayerScreenEvent>
        get() = _event
    private val _event = MutableSharedFlow<PlayerScreenEvent>()

    fun fetchDataIfNecessary(playlistId: Int) {
        val currentState = getCurrentInternalState()

        if (currentState is PlayerScreenViewModelInternalState.Loaded &&
            currentState.playlist.uid == playlistId
        ) {
            return
        }
        viewModelScope.launch(dispatchersProvider.default) {
            getPlaylistWithTracksUseCase(playlistId = playlistId)
                .collect { result ->
                    when (result) {
                        is ResultOf.Success -> {
                            onPlaylistReceived(playlistWithTracks = result.value)
                        }

                        is ResultOf.Failure -> {
                            val internalState = PlayerScreenViewModelInternalState.Error(
                                playlistId = playlistId,
                                errorType = when (result.value) {
                                    GetPlaylistWithTracksError.PlaylistNotFound ->
                                        PlayerScreenViewModelInternalState.ErrorType.PLAYLIST_NOT_FOUND

                                    GetPlaylistWithTracksError.InvalidPlaylist ->
                                        PlayerScreenViewModelInternalState.ErrorType.INVALID_PLAYLIST
                                }
                            )
                            publishNewInternalState(internalState)
                        }
                    }
                }
        }
    }

    fun onIntent(intent: PlayerScreenViewModelIntent) {
        when (intent) {
            is PlayerScreenViewModelIntent.OnTrackClick ->
                onTrackClick(trackId = intent.trackId)

            PlayerScreenViewModelIntent.OnCurrentTrackFinished ->
                onCurrentTrackFinished()

            is PlayerScreenViewModelIntent.OnRemoveTrackClick ->
                onRemoveTrackClick(trackId = intent.trackId)

            PlayerScreenViewModelIntent.OnAddTrackClick ->
                onAddTrackClick()

            PlayerScreenViewModelIntent.OnLibraryDialogDismissRequest ->
                onLibraryDialogDismissRequest()

            is PlayerScreenViewModelIntent.OnNewTrackAddedToPlaylist ->
                onNewTrackAddedToPlaylist(trackId = intent.trackId)
        }
    }

    private fun onTrackClick(trackId: Int) {
        viewModelScope.launch(dispatchersProvider.default) {
            val currentState = getCurrentInternalState()
            if (currentState !is PlayerScreenViewModelInternalState.Loaded) {
                return@launch
            }
            val selectedTrack = currentState.tracks.firstOrNull { it.uid == trackId }
            if (selectedTrack == null) {
                ErrorLogger.logError(
                    message = "onTrackClick: Track with ID $trackId not found in the playlist.",
                )
                _event.emit(
                    PlayerScreenEvent.ShowError(
                        message = TextResource.FromStringResource(R.string.generic_error),
                    )
                )
                return@launch
            } else {
                playTrack(track = selectedTrack)
            }
        }
    }

    private fun onRemoveTrackClick(trackId: Int) {
        viewModelScope.launch(dispatchersProvider.default) {
            val currentState = getCurrentInternalState()
            if (currentState !is PlayerScreenViewModelInternalState.Loaded) {
                return@launch
            }
            val trackToRemove = currentState.tracks.firstOrNull { it.uid == trackId }

            if (trackToRemove == null) {
                ErrorLogger.logError(
                    message = "onRemoveTrackClick: Track with ID $trackId not found in the playlist.",
                )
                _event.emit(
                    PlayerScreenEvent.ShowError(
                        message = TextResource.FromStringResource(R.string.generic_error),
                    )
                )
                return@launch
            }

            if (trackToRemove == currentState.selectedTrack) {
                // If the track to remove is currently selected, we need to stop playing it before removing
                _event.emit(PlayerScreenEvent.StopAndUnloadAllTracks)
            }

            removeTrackFromPlaylistUseCase(
                trackId = trackId,
                playlistId = currentState.playlist.uid,
            )
        }
    }

    private fun onAddTrackClick() {
        val currentState = getCurrentInternalState()

        if (currentState !is PlayerScreenViewModelInternalState.Loaded) {
            return
        }

        val newState = currentState.copy(
            isLibraryDialogVisible = true,
        )
        publishNewInternalState(newState)
    }

    private fun onLibraryDialogDismissRequest() {
        val currentState = getCurrentInternalState()

        if (currentState !is PlayerScreenViewModelInternalState.Loaded) {
            return
        }

        val newState = currentState.copy(
            isLibraryDialogVisible = false,
        )
        publishNewInternalState(newState)
    }

    private fun onNewTrackAddedToPlaylist(trackId: Int) {
        viewModelScope.launch(dispatchersProvider.default) {
            val currentState = getCurrentInternalState()
            if (currentState !is PlayerScreenViewModelInternalState.Loaded) {
                return@launch
            }

            addTrackToPlaylistUseCase(
                trackId = trackId,
                playlistId = currentState.playlist.uid,
            ).onSuccess {
                publishNewInternalState(
                    currentState.copy(
                        isLibraryDialogVisible = false,
                    )
                )
            }.onFailure {
                ErrorLogger.logError(
                    message = "onNewTrackAddedToPlaylist: Failed to add track with ID $trackId to playlist with ID ${currentState.playlist.uid}.",
                )
                _event.emit(
                    PlayerScreenEvent.ShowError(
                        message = TextResource.FromStringResource(R.string.generic_error),
                    )
                )
            }
        }
    }

    private fun playTrack(track: TrackDomainModel) {
        viewModelScope.launch(dispatchersProvider.default) {
            val currentState = getCurrentInternalState()
            if (currentState !is PlayerScreenViewModelInternalState.Loaded) {
                return@launch
            }
            val newState = currentState.copy(
                selectedTrack = track,
            )
            publishNewInternalState(newState)
            _event.emit(
                PlayerScreenEvent.PlayTrack(
                    trackUri = track.trackUri,
                ),
            )
        }
    }

    private fun onCurrentTrackFinished() {
        viewModelScope.launch(dispatchersProvider.default) {
            val currentState = getCurrentInternalState()
            if (currentState !is PlayerScreenViewModelInternalState.Loaded) {
                return@launch
            }

            val currentPlayingTrack = currentState.selectedTrack
                ?: run {
                    ErrorLogger.logError(
                        message = "onCurrentTrackFinished: No track is currently selected.",
                    )
                    _event.emit(
                        PlayerScreenEvent.ShowError(
                            message = TextResource.FromStringResource(R.string.generic_error),
                        )
                    )
                    return@launch
                }

            val nextTrack: TrackDomainModel? = currentState.tracks
                .zipWithNext()
                .firstOrNull { (first, _) ->
                    first.uid == currentPlayingTrack.uid
                }?.second

            if (nextTrack == null) {
                // last track in the playlist
                val newState = currentState.copy(
                    selectedTrack = null,
                )
                publishNewInternalState(newState)
                _event.emit(PlayerScreenEvent.StopAndUnloadAllTracks)
            } else {
                playTrack(nextTrack)
            }
        }
    }

    private fun onPlaylistReceived(
        playlistWithTracks: PlaylistWithTracksDomainModel,
    ) {
        val currentState = getCurrentInternalState()
        val newState = if (currentState is PlayerScreenViewModelInternalState.Loaded) {
            // To keep the selected track if it exists, and if it's still in the playlist
            val selectedTrack = currentState.selectedTrack?.takeIf { selectedTrack ->
                playlistWithTracks.tracks.contains(selectedTrack)
            }
            currentState.copy(
                playlist = playlistWithTracks.playlist,
                tracks = playlistWithTracks.tracks,
                selectedTrack = selectedTrack,
            )
        } else {
            PlayerScreenViewModelInternalState.Loaded(
                playlist = playlistWithTracks.playlist,
                tracks = playlistWithTracks.tracks,
            )
        }
        publishNewInternalState(newState)
    }

    private fun getCurrentInternalState(): PlayerScreenViewModelInternalState =
        savedStateHandle[INTERNAL_STATE_KEY]
            ?: PlayerScreenViewModelInternalState.InitialLoading

    private fun publishNewInternalState(
        internalState: PlayerScreenViewModelInternalState,
    ) {
        savedStateHandle[INTERNAL_STATE_KEY] = internalState
    }
}
