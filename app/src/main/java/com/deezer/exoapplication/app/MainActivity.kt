package com.deezer.exoapplication.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.deezer.exoapplication.database.debug.PopulateDatabaseManager
import com.deezer.exoapplication.presentation.root.RootComposable
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val populateDatabaseManager: PopulateDatabaseManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Just for this exercise, we will populate the database:
        lifecycleScope.launch {
            populateDatabaseManager.populate()

            setContent {
                RootComposable()
            }
        }
    }
}
