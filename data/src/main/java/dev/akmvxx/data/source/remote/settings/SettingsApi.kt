package dev.akmvxx.data.source.remote.settings

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SettingsApi {

    @GET("v1/apps/{id}")
    suspend fun loadConfiguration(@Path("id") id: Int): Response<SettingsResponse>

}
