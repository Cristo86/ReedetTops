package ar.com.cristianduarte.reedettops.ui.main

import androidx.lifecycle.*
import ar.com.cristianduarte.reedettops.entity.RedditPost
import ar.com.cristianduarte.reedettops.repository.RedditPostsRepository

class MainViewModel(repository: RedditPostsRepository) : ViewModel() {

    val redditPosts: LiveData<List<RedditPost>> = repository.redditPosts().asLiveData()

}
