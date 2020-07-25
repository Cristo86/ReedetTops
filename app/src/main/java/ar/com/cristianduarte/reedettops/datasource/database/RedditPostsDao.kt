package ar.com.cristianduarte.reedettops.datasource.database

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ar.com.cristianduarte.reedettops.entity.RedditPost
import kotlinx.coroutines.flow.Flow

@Dao
interface RedditPostsDao {

    @Query("SELECT * from reddit_posts ORDER BY idx asc")
    fun get(): DataSource.Factory<Int, RedditPost>

    @Query("DELETE from reddit_posts where id=:postId")
    suspend fun delete(postId: String)

    @Query("SELECT * from reddit_posts where id=:postId")
    suspend fun get(postId: String) : RedditPost

    @Query("DELETE from reddit_posts")
    suspend fun deleteAll()

    @Query("UPDATE reddit_posts SET locally_read = 1 where id=:postId")
    suspend fun markAsRead(postId: String)

    @Insert
    suspend fun insert(redditPost: RedditPost)

    @Insert(onConflict = OnConflictStrategy.IGNORE) // TODO Double check this as it's hiding a problem
    suspend fun insert(redditPosts: Collection<RedditPost>)
}