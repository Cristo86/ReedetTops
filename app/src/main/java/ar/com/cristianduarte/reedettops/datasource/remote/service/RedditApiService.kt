package ar.com.cristianduarte.reedettops.datasource.remote.service

import ar.com.cristianduarte.reedettops.datasource.remote.entity.RedditPostsResponse
import retrofit2.http.*

interface RedditApiService {

    @GET("top/.json")
    suspend fun top(@Query("limit") limit: Int, @Query("after") after: String): RedditPostsResponse

}