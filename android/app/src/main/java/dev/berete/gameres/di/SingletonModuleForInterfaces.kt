package dev.berete.gameres.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.berete.gameres.data_sources.remote.IGDBAPIClient
import dev.berete.gameres.domain.data_providers.remote.GameDetailsProvider
import dev.berete.gameres.domain.data_providers.remote.GameListProvider

@Module
@InstallIn(SingletonComponent::class)
abstract class SingletonModuleForInterfaces {

    @Binds
    abstract fun bindsGameListProvider(iGDBAPIClient: IGDBAPIClient): GameListProvider

    @Binds
    abstract fun bindsGameDetailsProvider(iGDBAPIClient: IGDBAPIClient): GameDetailsProvider

}