package com.toufiq.pokedexapp.repository

import com.toufiq.pokedexapp.data.remote.PokemonApi
import com.toufiq.pokedexapp.data.remote.responses.Pokemon
import com.toufiq.pokedexapp.data.remote.responses.PokemonList
import com.toufiq.pokedexapp.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@ActivityScoped
class PokemonRepository @Inject constructor(private val pokemonApi: PokemonApi) {

//
//    private val _pokemonList = MutableStateFlow<PokemonList?>(null)
//    val pokemonList: StateFlow<PokemonList?>
//        get() = _pokemonList

    suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonList> {
        val response = try {
            pokemonApi.getPokemonList(limit, offset)
        } catch (exception: Exception) {
            return Resource.Error(message = exception.message!!)
        }
        return Resource.Success(response)
    }

//    suspend fun getPokemonListV2(limit: Int, offset: Int){
//        val response = pokemonApi.getPokemonListV2(limit, offset)
//        if (response.isSuccessful && response.body() != null) {
//            _pokemonList.emit(response.body())
//        }
//    }

    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon> {
        val response = try {
            pokemonApi.getPokemonInfo(pokemonName)
        } catch (exception: Exception) {
            return Resource.Error(message = exception.message!!)
        }
        return Resource.Success(response)
    }
}