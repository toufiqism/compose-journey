package com.toufiq.pokedexapp.di

import com.toufiq.pokedexapp.data.remote.PokemonApi
import com.toufiq.pokedexapp.repository.PokemonRepository
import com.toufiq.pokedexapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun providePokemonRepo(api: PokemonApi) =
        PokemonRepository(api)

    @Singleton
    @Provides
    fun providePokeApi(): PokemonApi {
        return Retrofit.Builder().baseUrl(Constants.baseURL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(PokemonApi::class.java)
    }
}