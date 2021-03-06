package ar.com.cristianduarte.reedettops.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import ar.com.cristianduarte.reedettops.entity.RedditPost
import ar.com.cristianduarte.reedettops.repository.RedditPostsRepository
import kotlinx.coroutines.launch

class MainViewModel(val repository: RedditPostsRepository) : ViewModel() {

    val pagedRedditPosts =
        LivePagedListBuilder(
            repository.redditPosts(),
            PagedList.Config.Builder().setPageSize(20).setPrefetchDistance(1).build()
        ).setBoundaryCallback(PostsBoundaryCallback())
            .build()

    val fetchIndicator: MutableLiveData<Boolean> = MutableLiveData(false)

    suspend fun fetchRedditPosts(reset: Boolean) {
        repository.redditPostsFetch(reset)
    }

    fun dismissPost(redditPost: RedditPost) {
        viewModelScope.launch {
            repository.dismissPost(redditPost)
        }
    }

    fun dismissAllPosts() {
        viewModelScope.launch {
            repository.dismissAllPosts()
        }
    }

    fun startupFetch() {
        viewModelScope.launch {
            fetchIndicator.postValue(true)
            fetchRedditPosts(true)
            fetchIndicator.postValue(false)
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            fetchRedditPosts(true)
        }
    }

    inner class PostsBoundaryCallback(): PagedList.BoundaryCallback<RedditPost>() {
        override fun onItemAtEndLoaded(itemAtEnd: RedditPost) {
            viewModelScope.launch {
                repository.redditPostsFetch(false)
            }
        }
    }
}
