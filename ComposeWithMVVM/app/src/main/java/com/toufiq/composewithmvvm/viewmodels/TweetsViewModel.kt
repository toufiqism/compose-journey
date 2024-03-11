package com.toufiq.composewithmvvm.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toufiq.composewithmvvm.models.TweetListItem
import com.toufiq.composewithmvvm.repository.TweetRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TweetsViewModel @Inject constructor(private val tweetRepository: TweetRepository):ViewModel() {

    val tweets: StateFlow<List<TweetListItem>>
        get() = tweetRepository.tweets


    init {
        viewModelScope.launch {
            tweetRepository.getTweets("android")
        }
    }
}