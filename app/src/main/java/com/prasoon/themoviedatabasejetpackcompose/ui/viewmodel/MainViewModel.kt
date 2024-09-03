package com.prasoon.themoviedatabasejetpackcompose.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prasoon.themoviedatabasejetpackcompose.common.Resource
import com.prasoon.themoviedatabasejetpackcompose.domain.model.Movie
import com.prasoon.themoviedatabasejetpackcompose.domain.model.PopularMovies
import com.prasoon.themoviedatabasejetpackcompose.domain.use_case.GetPopularMoviesUseCase
import com.prasoon.themoviedatabasejetpackcompose.domain.use_case.SearchMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase,
    //private val repository: MoviesRepository,
) : ViewModel() {

    private val TAG = "MainViewModel"

    private val _popularMovies = MutableStateFlow<List<Movie>>(emptyList())
    val popularMovies: StateFlow<List<Movie>> = _popularMovies

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _searchMoviesText = MutableStateFlow("") // VM will change this
    val searchMoviesText = _searchMoviesText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    var currentPage = 1
    var totalPages = 1

    fun processIntent(intent: MovieViewIntent) {
        when (intent) {
            MovieViewIntent.LoadPopularMovies -> loadPopularMovies()
            is MovieViewIntent.SearchMovies -> onSearchMovieChanged(intent.query)
        }
    }

    private val _searchMovieList = MutableStateFlow<List<Movie>>(emptyList())

    //val searchMovieList: StateFlow<List<Movie>> = _searchMovieList
    // Always be called whenever searchtext and persons change
    @OptIn(FlowPreview::class)
    val searchMovieList = _searchMoviesText
        .debounce(300)
        .filter { searchPrefix -> (searchPrefix.length > 2) }
        .onEach { _isSearching.update { true } }    // searching to true
        .distinctUntilChanged() // to avoid duplicate network calls
        .combine(_searchMovieList) { currentSearchText, persons ->
            if (currentSearchText.isBlank()) {
                Log.d(TAG, "currentSearchText.isBlank: $persons")
                persons // show all list
            } else {
                // Filter it with the searchtext
                val resource = searchMoviesUseCase.invoke(query = currentSearchText)
                when (resource) {
                    is Resource.Failure -> TODO()
                    Resource.Loading -> TODO()
                    is Resource.Success -> {
                        val popularMovies = resource.result
                        _searchMovieList.value = popularMovies.movies
                        Log.d(TAG, "_searchMovieList: ${_searchMovieList.value}")
                    }
                }
                persons.filter {
                    it.doesMatchSearchQuery(currentSearchText)
                }
            }
        }
        .onEach { _isSearching.update { false } }   // search updated to false
        .stateIn(  // to convert it into StateFlow
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),   // If UI disappears, then this block is still active for 5 seconds
            _searchMovieList.value  // Initial value
        )

    fun onSearchMovieChanged(query: String) {
        _searchMoviesText.value = query
    }

    init {
        loadPopularMovies()
    }

    fun loadPopularMovies() {
        Log.i(TAG, "loadPopularMovies()")

        if (_isLoading.value || currentPage > totalPages) return

        viewModelScope.launch {
            _isLoading.value = true

            val resource = getPopularMoviesUseCase.invoke(currentPage)
            Log.i(TAG, "loadPopularMovies() resource: $resource")

            when (resource) {
                is Resource.Success -> {
                    val popularMovies = resource.result
                    if (popularMovies.movies.isEmpty()) {
                        _errorMessage.value = "No movies found"
                    } else {
                        _popularMovies.value += popularMovies.movies  // Append new data
                        totalPages = popularMovies.totalPages  // Update total pages
                        currentPage++  // Increment to the next page
                    }
                }

                is Resource.Failure -> {
                    _errorMessage.value = resource.exception.message  // Set the error message
                }

                is Resource.Loading -> {
                    _isLoading.value = false
                }
            }
            _isLoading.value = false
        }
    }

    fun getMovieById(movieId: Int?): Movie? {
        Log.i(TAG, "Got the movieId: $movieId")
        Log.i(TAG, "Got the movieId: ${_popularMovies.value}")

        val movie = _popularMovies.value.find { it.id == movieId }
        Log.i(TAG, "Got the MOVIE: ${movie}")

        return movie

    }

}