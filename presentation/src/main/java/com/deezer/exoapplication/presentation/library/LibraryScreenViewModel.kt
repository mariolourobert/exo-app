package com.deezer.exoapplication.presentation.library

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deezer.exoapplication.domain.usecases.GetAllTracksUseCase
import com.deezer.exoapplication.utils.mapStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LibraryScreenViewModel(
    private val getAllTracksUseCase: GetAllTracksUseCase,
    private val libraryScreenUiStateMapper: LibraryScreenUiStateMapper,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    companion object {
        private const val INTERNAL_STATE_KEY = "internal_state_key"
    }

    val uiState: StateFlow<LibraryScreenUiState> =
        savedStateHandle.getStateFlow<LibraryScreenViewModelInternalState>(
            key = INTERNAL_STATE_KEY,
            initialValue = LibraryScreenViewModelInternalState.InitialLoading,
        ).mapStateFlow(
            coroutineScope = viewModelScope,
            transform = libraryScreenUiStateMapper::toUiState,
        )

    fun fetchDataIfNecessary() {
        val currentState = getCurrentInternalState()

        if (currentState is LibraryScreenViewModelInternalState.Loaded) {
            return
        }

        viewModelScope.launch {
            val tracks = getAllTracksUseCase()
            publishNewState(
                newState = LibraryScreenViewModelInternalState.Loaded(
                    tracks = tracks,
                )
            )
        }
    }

    private fun publishNewState(newState: LibraryScreenViewModelInternalState) {
        savedStateHandle[INTERNAL_STATE_KEY] = newState
    }

    private fun getCurrentInternalState(): LibraryScreenViewModelInternalState =
        savedStateHandle.get<LibraryScreenViewModelInternalState>(INTERNAL_STATE_KEY)
            ?: LibraryScreenViewModelInternalState.InitialLoading
}
