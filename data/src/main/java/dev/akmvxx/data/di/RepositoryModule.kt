package dev.akmvxx.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.akmvxx.data.repository.BugRepositoryImpl
import dev.akmvxx.data.repository.FavoriteRepositoryImpl
import dev.akmvxx.data.repository.ModRepositoryImpl
import dev.akmvxx.data.repository.ProposeRepositoryImpl
import dev.akmvxx.data.repository.SaveRepositoryImpl
import dev.akmvxx.domain.repository.BugRepository
import dev.akmvxx.domain.repository.FavoriteRepository
import dev.akmvxx.domain.repository.ModRepository
import dev.akmvxx.domain.repository.ProposeRepository
import dev.akmvxx.domain.repository.SaveRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBugRepository(impl: BugRepositoryImpl): BugRepository

    @Binds
    @Singleton
    abstract fun bindProposeRepository(impl: ProposeRepositoryImpl): ProposeRepository

    @Binds
    @Singleton
    abstract fun bindModRepository(impl: ModRepositoryImpl): ModRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository

    @Binds
    @Singleton
    abstract fun bindSaveRepository(impl: SaveRepositoryImpl): SaveRepository

}
