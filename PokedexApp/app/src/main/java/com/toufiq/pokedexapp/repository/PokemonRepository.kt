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

    /*
    *  private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>>
        get() = _categories


    private val _tweets = MutableStateFlow<List<TweetListItem>>(emptyList())
    val tweets: StateFlow<List<TweetListItem>>
        get() = _tweets

    suspend fun getCategories() {
        val response = tweetsyAPI.getCategories()
        if (response.isSuccessful && response.body() != null) {
            //
            _categories.emit(response.body()!!)
        }
    }

    suspend fun getTweets(category: String) {
        val response = tweetsyAPI.getTweets("tweets[?(@.category==\"$category\")]")
        if (response.isSuccessful && response.body() != null) {
            _tweets.emit(response.body()!!)
        }
    }
    *
    *
    * */

    private val _pokemonList = MutableStateFlow<PokemonList?>(null)
    val pokemonList: StateFlow<PokemonList?>
        get() = _pokemonList

    suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonList> {
        val response = try {
            pokemonApi.getPokemonList(limit, offset)
        } catch (exception: Exception) {
            return Resource.Error(message = exception.message!!)
        }
        return Resource.Success(response)
    }

    suspend fun getPokemonListV2(limit: Int, offset: Int){
        val response = pokemonApi.getPokemonListV2(limit, offset)
        if (response.isSuccessful && response.body() != null) {
            _pokemonList.emit(response.body())
        }
    }

    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon> {
        val response = try {
            pokemonApi.getPokemonInfo(pokemonName)
        } catch (exception: Exception) {
            return Resource.Error(message = exception.message!!)
        }
        return Resource.Success(response)
    }
}