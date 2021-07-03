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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {

    @Provides
    fun providesIGDBAPIClient(accessTokenProvider: AccessTokenProvider): IGDBAPIClient {
        lateinit var iGDBAPIClient: IGDBAPIClient
        val threadLock = Object()
        CoroutineScope(IO).launch {
            val accessToken = accessTokenProvider.getAccessToken()
            val iGdbWrapper = IGDBWrapper.apply {
                setCredentials(BuildConfig.CLIENT_ID, accessToken)
            }

            iGDBAPIClient = IGDBAPIClient(iGdbWrapper, APICalypse())
            synchronized(threadLock) {
                threadLock.notify()
            }
        }
        synchronized(threadLock) {
            threadLock.wait(3000)
        }
        return iGDBAPIClient
    }

    @Provides
    fun providesAccessTokenProvider(
        @ApplicationContext context: Context,
        backendClient: BackendClient,
    ): AccessTokenProvider {
        val sharedPreferences =
            context.getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE)
        return AccessTokenProvider(backendClient, sharedPreferences)
    }

    @Provides
    fun providesBackendClient(): BackendClient {
        return Retrofit.Builder()
            .baseUrl(BackendClient.BACKEND_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BackendClient::class.java)
    }

}