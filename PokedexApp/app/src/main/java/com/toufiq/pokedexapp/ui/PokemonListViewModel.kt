package com.toufiq.pokedexapp.ui

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.toufiq.pokedexapp.data.models.PokedexListEntry
import com.toufiq.pokedexapp.data.remote.responses.PokemonList
import com.toufiq.pokedexapp.data.remote.responses.Result
import com.toufiq.pokedexapp.repository.PokemonRepository
import com.toufiq.pokedexapp.util.Constants.PAGE_SIZE
import com.toufiq.pokedexapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(private val repository: PokemonRepository) :
    ViewModel() {

    fun calcDominantColor(drawable: Drawable, onFinished: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bmp).generate() {
            it?.dominantSwatch?.rgb?.let { color ->
                onFinished(Color(color))
            }
        }
    }

    private var curPage = 0

    var pokemonList = mutableStateOf<List<PokedexListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)


    init {
//        loadPokemonPaginated()
        viewModelScope.launch {
            repository.getPokemonListV2(PAGE_SIZE, curPage * PAGE_SIZE)
        }
    }

    fun loadPokemonPaginated() {
        isLoading.value = true
        viewModelScope.launch {
            when (val result: Resource<PokemonList> = repository.getPokemonList(PAGE_SIZE, curPage * PAGE_SIZE)) {
                is Resource.Success -> {
                    endReached.value = curPage * PAGE_SIZE >= result.data!!.count!!
                    val pokedexEntries = result.data.results!!.mapIndexed { index, entry ->
                        val number = if (entry!!.url!!.endsWith("/")) {
                            entry.url!!.dropLast(1).takeLastWhile { it.isDigit() }
                        } else {
                            entry.url!!.takeLastWhile { it.isDigit() }
                        }
                        val url =
                            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"

                        PokedexListEntry(entry.name!!.capitalize(Locale.ROOT), url, number.toInt())
                    }
                    curPage++

                    loadError.value = ""
                    isLoading.value = false
                    pokemonList.value += pokedexEntries
                }

                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }

                is Resource.Loading -> {

                }
            }

            
        }
    }

    fun convertResultsToPokedexEntries(results: List<Result?>?): List<PokedexListEntry> {
        val pokedexEntries = results!!.mapIndexed { index, entry ->
            val number = if (entry!!.url!!.endsWith("/")) {
                entry.url!!.dropLast(1).takeLastWhile { it.isDigit() }
            } else {
                entry.url!!.takeLastWhile { it.isDigit() }
            }
            val url =
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"

            PokedexListEntry(entry.name!!.capitalize(Locale.ROOT), url, number.toInt())
        }
        return pokedexEntries
    }


    val pokemonListV2: StateFlow<PokemonList?>
        get() = repository.pokemonList
}