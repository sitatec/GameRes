package dev.berete.gameres.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.api.igdb.apicalypse.APICalypse
import com.api.igdb.request.IGDBWrapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.berete.gameres.BuildConfig
import dev.berete.gameres.data_sources.remote.IGDBAPIClient
import dev.berete.gameres.data_sources.remote.access_token.AccessTokenProvider
import dev.berete.gameres.data_sources.remote.access_token.BackendClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {

    @Provides
    @Singleton
    suspend fun providesIGDBAPIClient(accessTokenProvider: AccessTokenProvider): IGDBAPIClient {
        val accessToken = accessTokenProvider.getAccessToken()
        val iGdbWrapper = IGDBWrapper.apply { setCredentials(BuildConfig.CLIENT_ID, accessToken) }
        return IGDBAPIClient(iGdbWrapper, APICalypse())
    }

    @Provides
    fun providesAccessTokenProvider(@ApplicationContext context: Context, backendClient: BackendClient): AccessTokenProvider {
        val sharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE)
        return AccessTokenProvider(backendClient, sharedPreferences)
    }

    @Provides
    fun providesBackendClient(): BackendClient {
        val baseUrl = "${BackendClient.BACKEND_BASE_URL}/${BuildConfig.BACKEND_ACCESS_KEY}"
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .build()
            .create(BackendClient::class.java)
    }

}