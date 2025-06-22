package com.deezer.exoapplication.presentation.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface TextResource {
    data class FromString(val value: String) : TextResource

    class FromStringResource(
        @StringRes val stringResourceId: Int,
        vararg val args: Any,
    ) : TextResource

    @Composable
    fun asString(): String {
        return when (this) {
            is FromString -> value
            is FromStringResource -> stringResource(stringResourceId, *args)
        }
    }
}
