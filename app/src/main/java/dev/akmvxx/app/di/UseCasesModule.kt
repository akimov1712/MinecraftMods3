package dev.akmvxx.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.akmvxx.domain.repository.BugRepository
import dev.akmvxx.domain.repository.FavoriteRepository
import dev.akmvxx.domain.repository.ModRepository
import dev.akmvxx.domain.repository.ProposeRepository
import dev.akmvxx.domain.repository.SaveRepository
import dev.akmvxx.domain.useCases.bug.ReportBugUseCase
import dev.akmvxx.domain.useCases.favorite.ChangeStatusFavoriteUseCase
import dev.akmvxx.domain.useCases.favorite.FetchFavoriteModsUseCase
import dev.akmvxx.domain.useCases.favorite.GetCountFavoriteUseCase
import dev.akmvxx.domain.useCases.mod.FetchFileSizeUseCase
import dev.akmvxx.domain.useCases.mod.FetchModUseCase
import dev.akmvxx.domain.useCases.mod.FetchModsUseCase
import dev.akmvxx.domain.useCases.propose.ProposeModUseCase
import dev.akmvxx.domain.useCases.save.SaveFileUseCase
import dev.akmvxx.domain.validation.bug.BugValidator
import dev.akmvxx.domain.validation.propose.ProposeValidator

@Module
@InstallIn(SingletonComponent::class)
internal object UseCasesModule {

    @Provides
    fun provideFetchModsUseCase(repository: ModRepository): FetchModsUseCase =
        FetchModsUseCase(repository)

    @Provides
    fun provideFetchModUseCase(repository: ModRepository): FetchModUseCase =
        FetchModUseCase(repository)

    @Provides
    fun provideFetchFileSizeUseCase(repository: ModRepository): FetchFileSizeUseCase =
        FetchFileSizeUseCase(repository)

    @Provides
    fun provideGetCountFavoriteUseCase(repository: FavoriteRepository): GetCountFavoriteUseCase =
        GetCountFavoriteUseCase(repository)

    @Provides
    fun provideFetchFavoriteModsUseCase(repository: FavoriteRepository): FetchFavoriteModsUseCase =
        FetchFavoriteModsUseCase(repository)

    @Provides
    fun provideChangeStatusFavoriteUseCase(repository: FavoriteRepository): ChangeStatusFavoriteUseCase =
        ChangeStatusFavoriteUseCase(repository)

    @Provides
    fun provideReportBugUseCase(
        repository: BugRepository,
        validator: BugValidator,
    ): ReportBugUseCase = ReportBugUseCase(repository, validator)

    @Provides
    fun provideProposeModUseCase(
        repository: ProposeRepository,
        validator: ProposeValidator,
    ): ProposeModUseCase = ProposeModUseCase(repository, validator)

    @Provides
    fun provideSaveFileUseCase(repository: SaveRepository): SaveFileUseCase =
        SaveFileUseCase(repository)

}