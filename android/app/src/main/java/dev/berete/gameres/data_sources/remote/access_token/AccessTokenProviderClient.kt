package dev.berete.gameres.data_sources.remote.access_token

import retrofit2.http.GET

/**
 * The access token provider endpoints
 */
interface AccessTokenProviderClient {
    // We don't need to parse the response to a specific type because the returned json is very
    // simple. i.e: { token: <the token here>}

    @GET("get")
    /**
     * Returns the access token in a Map with the key "token".
     */
    suspend fun getAccessToken() : Map<String, String>

    @GET("refresh")
    suspend fun refreshToken() : Map<String, String>
}