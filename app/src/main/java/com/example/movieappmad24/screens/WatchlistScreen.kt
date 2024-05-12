package com.example.movieappmad24.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.movieappmad24.viewmodels.MoviesViewModel
import com.example.movieappmad24.widgets.MovieList
import com.example.movieappmad24.widgets.SimpleBottomAppBar
import com.example.movieappmad24.widgets.SimpleTopAppBar

@Composable
fun WatchlistScreen(
    navController: NavController,
    moviesViewModel: MoviesViewModel
){
    // State Flow collecten, um ihn in einen Compose State zu verwandeln.
    val favoriteMovies by moviesViewModel.favoriteMovies.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            SimpleTopAppBar(title = "Your Watchlist")
        },
        bottomBar = {
            SimpleBottomAppBar(navController = navController)
        }
    ) { innerPadding ->

        MovieList(
            modifier = Modifier.padding(innerPadding),
            movies = favoriteMovies,
            navController = navController,
            viewModel = moviesViewModel
        )
    }
}
