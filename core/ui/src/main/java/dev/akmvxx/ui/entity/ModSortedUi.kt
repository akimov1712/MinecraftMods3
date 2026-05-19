package dev.akmvxx.ui.entity

import androidx.annotation.StringRes
import dev.akmvxx.domain.entity.mod.ModSorted
import dev.akmvxx.ui.R

enum class ModSortedUi(
    @StringRes val titleRes: Int
) {
    Relevance(R.string.mod_sorted_relevance),
    MostPopular(R.string.mod_sorted_most_popular),
    Rating(R.string.mod_sorted_rating);

    fun toModSorted() = when(this){
        Relevance -> ModSorted.Relevance
        MostPopular -> ModSorted.MostPopular
        Rating -> ModSorted.Rating
    }
}
