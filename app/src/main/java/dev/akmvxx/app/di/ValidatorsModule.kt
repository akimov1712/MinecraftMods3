package dev.akmvxx.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.akmvxx.domain.repository.BugRepository
import dev.akmvxx.domain.repository.FavoriteRepository
import dev.akmvxx.domain.repository.ModRepository
import dev.akmvxx.domain.repository.ProposeRepository
import dev.akmvxx.domain.useCases.bug.ReportBugUseCase
import dev.akmvxx.domain.useCases.favorite.ChangeStatusFavoriteUseCase
import dev.akmvxx.domain.useCases.favorite.FetchFavoriteModsUseCase
import dev.akmvxx.domain.useCases.favorite.GetCountFavoriteUseCase
import dev.akmvxx.domain.useCases.mod.FetchModUseCase
import dev.akmvxx.domain.useCases.mod.FetchModsUseCase
import dev.akmvxx.domain.useCases.propose.ProposeModUseCase
import dev.akmvxx.domain.validation.bug.BugValidator
import dev.akmvxx.domain.validation.propose.ProposeValidator

@Module
@InstallIn(SingletonComponent::class)
internal object ValidatorsModule {

    @Provides
    fun provideBugValidator(): BugValidator = BugValidator()

    @Provides
    fun provideProposeValidator(): ProposeValidator = ProposeValidator()


}