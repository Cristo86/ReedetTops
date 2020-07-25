package ar.com.cristianduarte.reedettops.repository

import ar.com.cristianduarte.reedettops.datasource.database.RedditPostsDao
import ar.com.cristianduarte.reedettops.datasource.database.RedditPostsDatabase
import ar.com.cristianduarte.reedettops.datasource.remote.service.RedditApiDatasource
import ar.com.cristianduarte.reedettops.entity.RedditPost
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RedditPostsRepository(val redditApiDatasource: RedditApiDatasource, val redditPostsDao: RedditPostsDao) {

    companion object {
        const val PAGE_SIZE = 20
    }

    private var after = ""
    private var count = 0

    fun redditPosts() = redditPostsDao.get()

    suspend fun redditPostsFetch(reset: Boolean) {
        if (reset) {
            after = ""
            count = 0
        }

        val top = redditApiDatasource.top(PAGE_SIZE, after)

        val redditPostsToInsert = top.data.children.mapIndexed { index, redditPostData ->
            redditPostData.data.index = count + index
            redditPostData.data
        }
        after = top.data.after?:""
        count += top.data.children.size

        if (reset) {
            redditPostsDao.deleteAll()
        }

        redditPostsDao.insert(redditPostsToInsert)
    }

    fun redditPostsOld(): Flow<List<RedditPost>> = flow {
        // TODO Save data to a DB and then make that DB the only source of truth
        val redditPostsResponse = redditApiDatasource.top(20, "")
        emit (redditPostsResponse.data.children.map {
            it.data
        })
    }

}