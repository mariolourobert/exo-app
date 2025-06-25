package com.deezer.exoapplication.utils

import android.util.Log

object ErrorLogger {
    fun logError(
        message: String,
    ) {
        Log.e(
            "ErrorLogger",
            message,
        )
        // In a real application, we should also send this error to a crash reporting tool as Crashlytics
    }
}
