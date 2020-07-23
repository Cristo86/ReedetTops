package ar.com.cristianduarte.reedettops.entity

import com.google.gson.annotations.SerializedName

data class RedditPost (
    @SerializedName("id")
    val id: String,

    @SerializedName("author")
    val author: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("num_comments")
    val numComments: Int,

    @SerializedName("created_utc")
    val createdUtc: Long,

    @SerializedName("thumbnail")
    val thumbnail: String
)