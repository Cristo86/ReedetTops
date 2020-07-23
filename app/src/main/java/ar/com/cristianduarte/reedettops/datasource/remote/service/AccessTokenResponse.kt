package ar.com.cristianduarte.reedettops.datasource.remote.service

import com.google.gson.annotations.SerializedName

data class AccessTokenResponse(
    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("token_type")
    val tokenType: String,

    @SerializedName("device_id")
    val deviceId: String,

    @SerializedName("expires_in")
    val expiresIn: Int,

    @SerializedName("scope")
    val scope: String
)