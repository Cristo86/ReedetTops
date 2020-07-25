package ar.com.cristianduarte.reedettops.datasource.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ar.com.cristianduarte.reedettops.entity.RedditPost
import kotlinx.coroutines.flow.Flow

@Dao
interface RedditPostsDao {

    @Query("SELECT * from reddit_posts")
    fun get(): Flow<List<RedditPost>>

    @Query("DELETE from reddit_posts where id=:postId")
    suspend fun delete(postId: String)

    @Query("DELETE from reddit_posts")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(redditPost: RedditPost)

    @Insert
    suspend fun insert(redditPosts: Collection<RedditPost>)
}