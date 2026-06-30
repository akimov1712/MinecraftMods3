package dev.akmvxx.domain.entity.mod

enum class ModSorted {

    Relevance,
    MostPopular,
    Rating;

    fun toKeySorted() = when(this){
        Relevance -> "order"
        MostPopular -> "usedCount"
        Rating -> "rating"
    }
}
