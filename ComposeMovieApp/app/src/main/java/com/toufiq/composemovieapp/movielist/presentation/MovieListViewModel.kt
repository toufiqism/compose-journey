package com.toufiq.composemovieapp.movielist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedapps.moviesapp.movieList.util.Category
import com.toufiq.composemovieapp.movielist.domain.repository.MovieListRepository
import com.toufiq.composemovieapp.movielist.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(private val movieListRepository: MovieListRepository) :
    ViewModel() {
    private var _movieListState = MutableStateFlow(MovieListState())
    val movieListState = _movieListState.asStateFlow()

    init {
        getPopularMovies(false)
        getUpcomingMovies(false)
    }

    fun onEvent(event: MovieListUIEvent) {
        when (event) {
            MovieListUIEvent.Navigate -> {
                _movieListState.update {
                    it.copy(
                        isCurrentPopularScreen = !movieListState.value.isCurrentPopularScreen
                    )
                }
            }

            is MovieListUIEvent.Paginate -> {
                if (event.category == Category.POPULAR) {
                    getPopularMovies(true)
                } else if (event.category == Category.UPCOMING) {
                    getUpcomingMovies(true)
                }
            }
        }
    }

    private fun getUpcomingMovies(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }
            movieListRepository.getMovieList(
                forceFetchFromRemote, Category.UPCOMING, movieListState.value.upcomingMovieListPage
            ).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _movieListState.update { it.copy(isLoading = false) }
                    }

                    is Resource.Loading -> {
                        _movieListState.update { it.copy(isLoading = result.isLoading) }
                    }

                    is Resource.Success -> {
                        result.data?.let { upcomingList ->
                            _movieListState.update {
                                it.copy(
                                    upcomingMovieList = movieListState.value.upcomingMovieList + upcomingList.shuffled(),
                                    upcomingMovieListPage = movieListState.value.upcomingMovieListPage + 1

                                )
                            }
                        }
                    }
                }

            }
        }
    }

    private fun getPopularMovies(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }
            movieListRepository.getMovieList(
                forceFetchFromRemote, Category.POPULAR, movieListState.value.popularMovieListPage
            ).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _movieListState.update { it.copy(isLoading = false) }
                    }

                    is Resource.Loading -> {
                        _movieListState.update { it.copy(isLoading = result.isLoading) }
                    }

                    is Resource.Success -> {
                        result.data?.let { popularList ->
                            _movieListState.update {
                                it.copy(
                                    popularMovieList = movieListState.value.popularMovieList + popularList.shuffled(),
                                    popularMovieListPage = movieListState.value.popularMovieListPage + 1

                                )
                            }
                        }
                    }
                }

            }
        }
    }
}