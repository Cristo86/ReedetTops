package ar.com.cristianduarte.reedettops.datasource.remote.entity

import ar.com.cristianduarte.reedettops.entity.RedditPost

data class RedditPostsResponse (

    val kind: String,

    val data: RedditPostsResponseData
)

data class RedditPostsResponseData (
    val after: String?,

    val before: String?,

    val children: List<RedditPostData>
)

data class RedditPostData (
    val kind: String,

    val data: RedditPost
)