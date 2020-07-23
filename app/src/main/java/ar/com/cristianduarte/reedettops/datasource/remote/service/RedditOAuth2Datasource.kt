package ar.com.cristianduarte.reedettops.datasource.remote.service

import ar.com.cristianduarte.reedettops.BuildConfig
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class RedditOAuth2Datasource {

    companion object {
        val DEVICE_ID = UUID.randomUUID().toString()
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.REDDIT_BASE_URL_OAUTH2)
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val redditOAuth2Service = retrofit.create(RedditOAuth2Service::class.java)

    suspend fun accessToken(): AccessTokenResponse {
        return redditOAuth2Service.accessToken(
            Credentials.basic(BuildConfig.REDDIT_CLIENT_ID, ""),
            "https://oauth.reddit.com/grants/installed_client",
            DEVICE_ID
        )
    }
}