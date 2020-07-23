package ar.com.cristianduarte.reedettops.datasource.remote.service

import ar.com.cristianduarte.reedettops.BuildConfig
import ar.com.cristianduarte.reedettops.datasource.remote.entity.RedditPostsResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RedditApiDatasource {

    private val oAuth2Datasource = RedditOAuth2Datasource()

    private val sessionManager = SessionManager()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.REDDIT_BASE_URL)
        .client(okHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val redditApiService = retrofit.create(RedditApiService::class.java)

    suspend fun top(limit: Int, after: String): RedditPostsResponse = authenticated {
        return@authenticated redditApiService.top(limit, after)
    }

    private suspend fun <T> authenticated(block: suspend () -> T) : T {
        // TODO make use of expires_at data from the token also
        if (sessionManager.fetchAccessToken() == null) {
            val accessToken = oAuth2Datasource.accessToken()
            sessionManager.saveAccessToken(accessToken.accessToken)
        }

        // TODO Check expired tokens exceptions or forbidden
        return block()
    }

    private fun okHttpClient() : OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(AuthInterceptor(sessionManager)).build()
    }
}

class SessionManager {
    private var cachedAccessToken: String? = null

    fun fetchAccessToken() = cachedAccessToken

    fun saveAccessToken(accessToken: String) {
        cachedAccessToken = accessToken
    }
}

class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        sessionManager.fetchAccessToken() ?.let {
            requestBuilder.addHeader("Authorization", "bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}