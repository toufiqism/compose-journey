package com.toufiq.pokedexapp.ui

import androidx.lifecycle.ViewModel
import com.toufiq.pokedexapp.data.remote.responses.Pokemon
import com.toufiq.pokedexapp.repository.PokemonRepository
import com.toufiq.pokedexapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(private val repository: PokemonRepository) :
    ViewModel() {

        suspend fun getPokemonInfo(pokemonName:String):Resource<Pokemon>{
            return repository.getPokemonInfo(pokemonName)
        }
}