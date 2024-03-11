package com.toufiq.composewithmvvm.models

class TweetList : ArrayList<TweetListItem>()


data class TweetListItem(
    var category: String?,
    var text: String?
)