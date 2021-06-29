package dev.berete.gameres.data_sources.remote.access_token

import android.content.SharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * The access token provider with cache system
 */
class AccessTokenProvider(
    private val backendClient: BackendClient,
    private val sharedPreferences: SharedPreferences,
) {
    private val accessTokenCacheKey = "API_ACCESS_TOKEN"

    private var accessToken: String = sharedPreferences.getString(accessTokenCacheKey, "")!!

    suspend fun getAccessToken(): String {
        if (accessToken.isEmpty()) {
            accessToken = fetchFromServerAndSave()
        }
        return accessToken
    }

    fun resetCache() {
        sharedPreferences.edit().remove(accessTokenCacheKey).apply()
    }

    suspend fun fetchFromServerAndSave() = fetchTokenFromServer().also {
        accessToken = it
        sharedPreferences.edit().putString(accessTokenCacheKey, it).apply()
    }


    private suspend fun fetchTokenFromServer() = withContext(IO) {
        backendClient.getAccessToken()["token"]!!
    }


}