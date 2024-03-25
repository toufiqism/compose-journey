package com.toufiq.composemovieapp.details.presantation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toufiq.composemovieapp.movielist.domain.repository.MovieListRepository
import com.toufiq.composemovieapp.movielist.presentation.MovieListState
import com.toufiq.composemovieapp.movielist.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val movieId = savedStateHandle.get<Int>("movieId")

    private val _detailsState = MutableStateFlow(DetailsState())
    val detailsState = _detailsState.asStateFlow()

    init {
        getMovie(movieId ?: -1)
    }

    private fun getMovie(movieId: Int) {
        viewModelScope.launch {
            _detailsState.update {
                it.copy(
                    isLoading = true
                )
            }
            movieListRepository.getMovie(movieId).collectLatest { res ->
                when (res) {
                    is Resource.Error -> {
                        _detailsState.update {
                            it.copy(
                                isLoading = false
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _detailsState.update {
                            it.copy(
                                isLoading = res.isLoading
                            )
                        }
                    }

                    is Resource.Success -> {
                        res.data?.let { movie ->
                            _detailsState.update {
                                it.copy(movie = movie)
                            }
                        }
                    }
                }
            }

        }
    }
}