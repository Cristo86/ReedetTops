package ar.com.cristianduarte.reedettops.datasource.database

import androidx.paging.DataSource
import androidx.room.*
import ar.com.cristianduarte.reedettops.datasource.database.entity.RedditPostPermanentInfo
import ar.com.cristianduarte.reedettops.entity.RedditPost

@Dao
abstract class RedditPostsDao {

    @Query("SELECT * from reddit_posts ORDER BY idx asc")
    abstract fun get(): DataSource.Factory<Int, RedditPost>

    @Query("DELETE from reddit_posts where id=:postId")
    abstract suspend fun delete(postId: String)

    @Query("SELECT * from reddit_posts where id=:postId")
    abstract suspend fun get(postId: String) : RedditPost

    @Query("DELETE from reddit_posts")
    abstract suspend fun deleteAll()

    @Query("UPDATE reddit_posts SET locally_read = 1 where id=:postId")
    abstract suspend fun markPostAsRead(postId: String)

    @Insert
    abstract suspend fun insert(redditPost: RedditPost)

    @Insert(onConflict = OnConflictStrategy.IGNORE) // TODO Double check this as it's hiding a problem
    abstract suspend fun insert(redditPosts: Collection<RedditPost>)

    @Query("SELECT * from reddit_post_permanent_info where id=:postId")
    abstract suspend fun getPermanentInfo(postId: String) : RedditPostPermanentInfo?

    @Insert()
    abstract suspend fun insertPermanentInfo(redditPostPermanentInfo: RedditPostPermanentInfo)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertPermanentInfo(redditPostPermanentInfos: List<RedditPostPermanentInfo>)

    @Query("UPDATE reddit_post_permanent_info SET locally_read=1 WHERE id=:postId")
    abstract suspend fun markAsReadPermanent(postId: String)

    @Query("UPDATE reddit_post_permanent_info SET dismissed=1 WHERE id=:postId")
    abstract suspend fun markAsDismissedPermanent(postId: String)

    @Transaction
    open suspend fun markAsRead(postId: String) {
        markPostAsRead(postId)
        markAsReadPermanent(postId)
    }

    @Transaction
    open suspend fun dismissPost(postId: String) {
        delete(postId)
        markAsDismissedPermanent(postId)
    }

    @Transaction
    open suspend fun insertPosts(
        redditPosts: Collection<RedditPost>,
        redditPostPermanentInfos: List<RedditPostPermanentInfo>
    ) {
        insert(redditPosts)
        insertPermanentInfo(redditPostPermanentInfos)
    }
}