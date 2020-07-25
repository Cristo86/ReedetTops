package ar.com.cristianduarte.reedettops.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

// Why @JvmOverloads
// https://www.coroutinedispatcher.com/2019/08/solving-room-cannot-find-setter-for.html

@Entity(tableName = "reddit_posts")
data class RedditPost @JvmOverloads constructor(

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
    var thumbnail: String,

    @ColumnInfo(name = "idx")
    var index: Int,

    @ColumnInfo(name = "locally_read")
    var locallyRead: Boolean,

    @SerializedName("permalink")
    @ColumnInfo(name = "permalink")
    var permalink: String,

    @Ignore
    @SerializedName("preview")
    var preview: PreviewElement? = null,

    @ColumnInfo(name = "image_url")
    var previewImageUrl: String?
)

data class PreviewElement (
    @SerializedName("images")
    var images: List<ImageElement>
)

data class ImageElement (
    @SerializedName("source")
    var source: ImageSource
)

data class ImageSource (
    @SerializedName("url")
    var url: String
)