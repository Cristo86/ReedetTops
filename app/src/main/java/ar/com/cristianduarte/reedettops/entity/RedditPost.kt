package ar.com.cristianduarte.reedettops.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "reddit_posts")
data class RedditPost (

    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    @ColumnInfo(name = "id")
    val id: String,

    @SerializedName("author")
    @ColumnInfo(name = "author")
    val author: String,

    @SerializedName("title")
    @ColumnInfo(name = "title")
    val title: String,

    @SerializedName("num_comments")
    @ColumnInfo(name = "num_comments")
    val numComments: Int,

    @SerializedName("created_utc")
    @ColumnInfo(name = "created_utc")
    val createdUtc: Long,

    @SerializedName("thumbnail")
    @ColumnInfo(name = "thumbnail")
    val thumbnail: String,

    @ColumnInfo(name = "idx")
    var index: Int,

    @ColumnInfo(name = "locally_read")
    var locallyRead: Boolean
)