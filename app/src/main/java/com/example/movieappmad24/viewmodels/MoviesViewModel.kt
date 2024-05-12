package com.example.movieappmad24.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappmad24.data.MovieRepository
import com.example.movieappmad24.models.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MoviesViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()

    // Konvertieren eines Flow in StateFlow
    val favoriteMovies: StateFlow<List<Movie>> = _movies.map { movies ->
        movies.filter { it.isFavorite }
    }.stateIn(
        scope = viewModelScope,  // Nutzung des viewModelScope
        started = SharingStarted.WhileSubscribed(5000L),  // Start-Strategie: nur solange es Abonnenten gibt, plus eine Gnadenfrist von 5000ms
        initialValue = emptyList()  // Anfangswert, wenn es keine Daten gibt
    )

    val isLoading = MutableStateFlow(true)

    init {
        viewModelScope.launch {
            repository.getAllMovies().distinctUntilChanged().collect { listOfMovies ->
                _movies.value = listOfMovies
                isLoading.value = false
            }
        }
    }


    fun toggleFavoriteMovie(movieId: String) {
        viewModelScope.launch {
            // Get a mutable copy of the current movies
            val updatedMovies = _movies.value.toMutableList()
            // Find the movie and toggle the favorite status
            val movieIndex = updatedMovies.indexOfFirst { it.id == movieId }
            if (movieIndex != -1) {
                val updatedMovie = updatedMovies[movieIndex].copy(isFavorite = !updatedMovies[movieIndex].isFavorite)
                updatedMovies[movieIndex] = updatedMovie
                _movies.value = updatedMovies // Push the updated list back to the state flow
            }
        }
    }
}

