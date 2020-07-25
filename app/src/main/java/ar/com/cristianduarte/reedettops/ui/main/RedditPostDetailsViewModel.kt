package ar.com.cristianduarte.reedettops.ui.main

import androidx.lifecycle.ViewModel
import ar.com.cristianduarte.reedettops.entity.RedditPost
import ar.com.cristianduarte.reedettops.repository.RedditPostsRepository

class RedditPostDetailsViewModel(val repository: RedditPostsRepository) : ViewModel()  {

    var currentRedditPost: RedditPost = NO_POST

    suspend fun redditPostById(id: String) {
        currentRedditPost = repository.redditPostById(id)
    }

    companion object {
        val NO_POST = RedditPost("","","",0,0,"", 0)
    }
}