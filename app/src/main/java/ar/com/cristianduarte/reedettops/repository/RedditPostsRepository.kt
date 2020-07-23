package ar.com.cristianduarte.reedettops.repository

import ar.com.cristianduarte.reedettops.datasource.remote.service.RedditApiDatasource
import ar.com.cristianduarte.reedettops.entity.RedditPost
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RedditPostsRepository {

    private val redditApiDatasource = RedditApiDatasource()

    fun redditPosts(): Flow<List<RedditPost>> = flow {
        // TODO Save data to a DB and then make that DB the only source of truth
        val redditPostsResponse = redditApiDatasource.top(20, "")
        emit (redditPostsResponse.data.children.map {
            it.data
        })
    }

}