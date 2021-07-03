package dev.berete.gameres.data_sources.remote.access_token

import dev.berete.gameres.BuildConfig
import retrofit2.http.GET

/**
 * The Backend endpoints
 */
interface BackendClient {
    // The returned json is very simple. i.e: { token: <the token here>}. So Map is sufficient.

    /**
     * Returns the access token in a Map with the key "token".
     */
    @GET("apitoken/${BuildConfig.BACKEND_ACCESS_KEY}/get")
    suspend fun getAccessToken() : Map<String, String>

    /**
     * Make a request to refresh the access token and return the new one.
     */
    @GET("apitoken/${BuildConfig.BACKEND_ACCESS_KEY}/refresh")
    suspend fun refreshToken() : Map<String, String>

    companion object {
        const val BACKEND_BASE_URL = BuildConfig.BACKEND_URL
    }
}