package com.deezer.exoapplication.presentation.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.deezer.exoapplication.presentation.R
import org.koin.androidx.compose.koinViewModel

private val TRACK_HEIGHT = 56.dp

@Composable
fun LibraryScreen(
    onTrackClick: (trackId: Int) -> Unit,
) {
    val viewModel = koinViewModel<LibraryScreenViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val onTrackClickLambda = remember {
        { trackUiModel: LibraryScreenUiState.TrackUiModel ->
            onTrackClick(trackUiModel.uid)
        }
    }

    when (val state = uiState) {
        is LibraryScreenUiState.Loading ->
            LoadingState()

        is LibraryScreenUiState.Loaded ->
            LoadedState(
                uiState = state,
                onTrackClick = onTrackClickLambda,
            )

        is LibraryScreenUiState.EmptyLibrary ->
            EmptyState()
    }

    LaunchedEffect(Unit) {
        viewModel.fetchDataIfNecessary()
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.libraryscreen_empty_state),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun LoadedState(
    uiState: LibraryScreenUiState.Loaded,
    onTrackClick: (LibraryScreenUiState.TrackUiModel) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(uiState.tracks) { track ->
            Track(
                trackUiModel = track,
                onTrackClick = onTrackClick,
            )
        }
    }
}

@Composable
private fun Track(
    trackUiModel: LibraryScreenUiState.TrackUiModel,
    onTrackClick: (LibraryScreenUiState.TrackUiModel) -> Unit,
) {
    val onTrackClickLambda = remember {
        {
            onTrackClick(trackUiModel)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(TRACK_HEIGHT)
            .clickable(onClick = onTrackClickLambda)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = trackUiModel.trackName,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        HorizontalDivider()
    }
}
