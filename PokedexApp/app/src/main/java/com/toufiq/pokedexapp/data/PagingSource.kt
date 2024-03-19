package com.toufiq.pokedexapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.toufiq.pokedexapp.data.remote.PokemonApi
import com.toufiq.pokedexapp.data.remote.responses.Result
import com.toufiq.pokedexapp.util.*
import com.toufiq.pokedexapp.util.Constants.PAGE_SIZE
import javax.inject.Inject

class PagingSource @Inject constructor(private val api: PokemonApi) : PagingSource<Int, Result>() {
    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        return try {
            val prev = params.key ?: 20
            val response = api.getPokemonListV2(limit = prev, offset = params.loadSize)

            if (response.isSuccessful) {
                val body = response.body()?.results
                LoadResult.Page(
                    data = body!!,
                    prevKey = if (prev == PAGE_SIZE) null else prev - PAGE_SIZE,
                    nextKey = if (prev == PAGE_SIZE) null else prev + PAGE_SIZE
                )
            } else {
                LoadResult.Error(Exception())
            }

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}