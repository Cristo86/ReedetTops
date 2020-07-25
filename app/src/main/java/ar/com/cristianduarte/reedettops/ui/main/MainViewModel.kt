package ar.com.cristianduarte.reedettops.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.toLiveData
import ar.com.cristianduarte.reedettops.entity.RedditPost
import ar.com.cristianduarte.reedettops.repository.RedditPostsRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(val repository: RedditPostsRepository) : ViewModel() {

    val redditPosts: LiveData<PagedList<RedditPost>> = repository.redditPosts().toLiveData(pageSize = 20)

    val pagedRedditPosts =
        LivePagedListBuilder(
            repository.redditPosts(),
            PagedList.Config.Builder().setPageSize(20).setPrefetchDistance(1).build()
        ).setBoundaryCallback(PostsBoundaryCallback())
            .build()


    fun fetchRedditPosts(reset: Boolean) {
        viewModelScope.launch {
            repository.redditPostsFetch(reset)
        }
    }

    fun dismissPost(redditPost: RedditPost) {
        viewModelScope.launch {
            repository.dismissPost(redditPost)
        }
    }

    fun onRefresh() {
        fetchRedditPosts(true)
    }

    inner class PostsBoundaryCallback(): PagedList.BoundaryCallback<RedditPost>() {
        override fun onItemAtEndLoaded(itemAtEnd: RedditPost) {
            viewModelScope.launch {
                repository.redditPostsFetch(false)
            }
        }
    }
}
