package com.toufiq.navigationexample.moviexample

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MovieViewModel : ViewModel() {

    private val dataSource = DataSource()
    private val _homeUiState = MutableStateFlow(HomeUiState(dataSource.generateMovieList()))
    val homeUiState = _homeUiState.asStateFlow()

//    private val _detailsUiState = MutableStateFlow(DetailsUiState())
//    val detailsUiState = _detailsUiState.asStateFlow(getMovieById())


    fun getMovieById(id: Long): StateFlow<DetailsUiState> {
        val movie = _homeUiState.value.movies.find { it.id == id }
        return MutableStateFlow(DetailsUiState(movie!!)).asStateFlow()
    }


    fun updateFavorites(movie: Movie) {
        _homeUiState.update { currentState ->
            val updatedList = currentState.movies.map { it ->
                if (it.id == movie.id) {
                    it.copy(favorite = !it.favorite)
                } else {
                    it
                }
            }
            HomeUiState(updatedList)
        }
    }
}


data class HomeUiState(val movies: List<Movie>)
data class DetailsUiState(val movie: Movie)