package com.toufiq.pokedexapp.data.remote

import com.toufiq.pokedexapp.data.remote.responses.Pokemon
import com.toufiq.pokedexapp.data.remote.responses.PokemonList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApi {

    @GET("/api/v2/pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonList

    @GET("/api/v2/pokemon/{pokemonName}")
    suspend fun getPokemonInfo(@Path("pokemonName") pokemonName: String): Pokemon
}