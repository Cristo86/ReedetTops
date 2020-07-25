package ar.com.cristianduarte.reedettops.ui.main

import androidx.lifecycle.*
import ar.com.cristianduarte.reedettops.entity.RedditPost
import ar.com.cristianduarte.reedettops.repository.RedditPostsRepository
import kotlinx.coroutines.launch

class MainViewModel(val repository: RedditPostsRepository) : ViewModel() {

    val redditPosts: LiveData<List<RedditPost>> = repository.redditPosts().asLiveData()

    fun fetchRedditPosts(reset: Boolean) {
        viewModelScope.launch {
            repository.redditPostsFetch(reset)
        }
    }

}
