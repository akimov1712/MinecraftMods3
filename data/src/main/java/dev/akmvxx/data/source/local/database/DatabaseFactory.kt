package dev.akmvxx.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteDbo::class],
    version = 1
)
internal abstract class DatabaseFactory: RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao

}
