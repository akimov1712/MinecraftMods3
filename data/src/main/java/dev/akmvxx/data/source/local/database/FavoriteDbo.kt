package dev.akmvxx.data.source.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("favorite_mods")
data class FavoriteDbo(
    val modId: Int,
    val status: Boolean,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
