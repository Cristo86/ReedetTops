package ar.com.cristianduarte.reedettops.repository

import ar.com.cristianduarte.reedettops.datasource.database.RedditPostsDao
import ar.com.cristianduarte.reedettops.datasource.database.entity.RedditPostPermanentInfo
import ar.com.cristianduarte.reedettops.datasource.remote.entity.RedditPostsResponse
import ar.com.cristianduarte.reedettops.datasource.remote.service.RedditApiDatasource
import ar.com.cristianduarte.reedettops.entity.RedditPost
import java.io.IOException

class RedditPostsRepository(val redditApiDatasource: RedditApiDatasource, val redditPostsDao: RedditPostsDao) {

    companion object {
        const val PAGE_SIZE = 20
    }

    private var after = ""
    private var count = 0

    fun redditPosts() = redditPostsDao.get()

    suspend fun redditPostsFetch(reset: Boolean) {
        val afterBack = after
        val countBack = count
        if (reset) {
            after = ""
            count = 0
        }
        val top: RedditPostsResponse
        try {
            top = redditApiDatasource.top(PAGE_SIZE, after)
        } catch (ioe: IOException) {
            after = afterBack
            count = countBack
            return
        }

        val redditPostsToInsert = top.data.children.mapIndexedNotNull { index, redditPostData ->
            val redditPost = redditPostData.data
            // Our own index
            redditPost.index = count + index

            // Move the image url outside
            redditPost.previewImageUrl = redditPost.preview?.images?.let {
                if (it.isEmpty()) return@let ""
                // Why &amp; https://old.reddit.com/r/redditdev/comments/9ncg2r/changes_in_api_pictures/
                return@let it[0].source.url.replace("&amp;","&")
            }

            // Get read and dismissed info..
            val permanentInfo = redditPostsDao.getPermanentInfo(redditPost.id) ?: return@mapIndexedNotNull redditPost

            if (permanentInfo.dismissed) {
                return@mapIndexedNotNull null
            }

            redditPost.locallyRead = permanentInfo.locallyRead

            return@mapIndexedNotNull redditPost
        }
        after = top.data.after?:""
        count += top.data.children.size

        if (reset) {
            redditPostsDao.deleteAll()
        }

        val redditPostsPermanentToInsert = redditPostsToInsert.map { redditPost ->
            RedditPostPermanentInfo(redditPost.id)
        }

        redditPostsDao.insertPosts(redditPostsToInsert, redditPostsPermanentToInsert)
    }

    suspend fun dismissPost(redditPost: RedditPost) {
        // if the user refreshes, this will be considered "permanently dismissed"
        redditPostsDao.dismissPost(redditPost.id)
    }

    suspend fun dismissAllPosts() {
        // As in case of accident, if the user refreshes, those won't be considered "permanently dismissed"
        redditPostsDao.deleteAll()
    }

    suspend fun redditPostById(id: String): RedditPost {
        return redditPostsDao.get(id)
    }

    suspend fun markRedditPostAsRead(id: String) {
        redditPostsDao.markAsRead(id)
    }
}