package dev.akmvxx.data.source.remote.mod

import dev.akmvxx.domain.entity.mod.ModCategory
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HEAD
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

internal interface ModApi {

    @GET("v1/mod/{id}")
    suspend fun fetchMod(
        @Path("id") modId: Int,
        @Header("Language") language: String
    ): Response<ModDto>

    @HEAD
    suspend fun fetchFileMeta(@Url url: String): Response<Void>

    @GET("v1/apps/{id}/mod/actived")
    suspend fun fetchMods(
        @Path("id") appId: Int,
        @Header("Language") language: String,
        @Query("q") searchQuery: String,
        @Query("category") category: ModCategory?,
        @Query("sort_key") keySorted: String,
        @Query("skip") offset: Int,
        @Query("take") take: Int,
        @Query("sort_value") sortValue: String = "asc",
    ): Response<ModsResponse>

}
