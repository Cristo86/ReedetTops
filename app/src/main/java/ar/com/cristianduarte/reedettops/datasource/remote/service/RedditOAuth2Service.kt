package ar.com.cristianduarte.reedettops.datasource.remote.service

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface RedditOAuth2Service {

    @POST("access_token")
    @FormUrlEncoded
    suspend fun accessToken(
        @Header("Authorization") authorization: String,
        @Field("grant_type") grantType: String,
        @Field("device_id") deviceId: String
    ) : AccessTokenResponse

}