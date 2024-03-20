package com.toufiq.pokedexapp.ui

import androidx.lifecycle.ViewModel
import com.toufiq.pokedexapp.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(private val repository: PokemonRepository) :
    ViewModel() {
}