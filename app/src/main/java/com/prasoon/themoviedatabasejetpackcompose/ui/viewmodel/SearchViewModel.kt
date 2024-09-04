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
class SearchViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase,
    //private val repository: MoviesRepository,
) : ViewModel() {

    private val TAG = "SearchViewModel"

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _searchMoviesText = MutableStateFlow("") // VM will change this
    val searchMoviesText = _searchMoviesText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()


    private val _searchMovieList = MutableStateFlow<List<Movie>>(emptyList())

    // Always be called whenever text is searched
    @OptIn(FlowPreview::class)
    val searchMovieList = _searchMoviesText
        .debounce(300)
        .filter { searchPrefix -> (searchPrefix.length > 2) }
        .onEach { _isSearching.update { true } }    // searching to true
        .distinctUntilChanged() // to avoid duplicate network calls
        .combine(_searchMovieList) { currentSearchText, movies ->
                // Filter it with the searchtext
                when (val resource = searchMoviesUseCase.invoke(query = currentSearchText)) {
                    is Resource.Failure -> _errorMessage.update { resource.exception.message }
                    Resource.Loading -> _isLoading.update { true }
                    is Resource.Success -> {
                        val popularMovies = resource.result
                        _searchMovieList.value = popularMovies.movies
                        Log.d(TAG, "_searchMovieList: ${_searchMovieList.value}")
                        _errorMessage.update { null }
                    }
                }
                movies.filter {
                    it.doesMatchSearchQuery(currentSearchText)
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

    fun getMovieById(movieId: Int?): Movie? {
        Log.i(TAG, "getMovieById() movieId: $movieId, searchMovieList: ${searchMovieList.value}")
        val movie = _searchMovieList.value.find { it.id == movieId }
        Log.i(TAG, "getMovieById() movie: $movie")
        return movie
    }
}