package dev.akmvxx.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.akmvxx.data.source.local.database.DatabaseFactory
import dev.akmvxx.data.source.local.database.FavoriteDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    private const val DB_NAME = "mods.db"

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DatabaseFactory =
        Room.databaseBuilder(context, DatabaseFactory::class.java, DB_NAME).build()

    @Provides
    fun provideFavoriteDao(database: DatabaseFactory): FavoriteDao = database.favoriteDao()

}
