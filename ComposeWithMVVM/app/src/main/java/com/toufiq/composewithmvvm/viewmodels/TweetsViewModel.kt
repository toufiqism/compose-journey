package com.toufiq.composewithmvvm.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toufiq.composewithmvvm.models.TweetListItem
import com.toufiq.composewithmvvm.repository.TweetRepository
import com.toufiq.composewithmvvm.ui.routes.Routes.ROUTES_CATEGORY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TweetsViewModel @Inject constructor(
    private val tweetRepository: TweetRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val tweets: StateFlow<List<TweetListItem>>
        get() = tweetRepository.tweets


    init {
        viewModelScope.launch {
            val category = savedStateHandle.get<String>(ROUTES_CATEGORY) ?: "android"
            tweetRepository.getTweets(category)
        }
    }
}