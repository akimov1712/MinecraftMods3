package dev.akmvxx.data.source.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT COUNT(*) FROM FAVORITE_MODS")
    suspend fun selectCountFavorites(): Int

    @Query("SELECT * FROM FAVORITE_MODS WHERE modId=:modId LIMIT 1")
    suspend fun selectFavorite(modId: Int): FavoriteDbo?

    @Query("SELECT modId FROM FAVORITE_MODS")
    suspend fun selectFavoriteIds(): List<Int>

    @Query("SELECT * FROM FAVORITE_MODS LIMIT :limit OFFSET :offset")
    suspend fun selectFavorites(limit: Int, offset: Int): List<FavoriteDbo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteDbo)

}