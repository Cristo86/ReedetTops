package ar.com.cristianduarte.reedettops.datasource.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ar.com.cristianduarte.reedettops.entity.RedditPost

@Database(entities = [RedditPost::class], version = 4, exportSchema = false)
abstract class RedditPostsDatabase : RoomDatabase() {
    /**
     * Connects the database to the DAO.
     */
    abstract val redditPostsDao: RedditPostsDao

    companion object {

        @Volatile
        private var INSTANCE: RedditPostsDatabase? = null

        fun getInstance(context: Context): RedditPostsDatabase {

            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RedditPostsDatabase::class.java,
                        "reddit_posts_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}