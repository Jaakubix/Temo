package com.movieshelf.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.movieshelf.app.ui.navigation.MovieShelfApp
import com.movieshelf.app.ui.theme.MovieShelfTheme
import com.movieshelf.app.ui.theme.MsBg

/**
 * Główne Activity — hostuje nawigację ekranów z makiety MovieShelf.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieShelfTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding(),
                    color = MsBg
                ) {
                    MovieShelfApp()
                }
            }
        }
    }
}
