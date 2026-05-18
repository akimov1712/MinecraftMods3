package dev.akmvxx.data.source.remote.propose

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

internal interface ProposeApi {

    @POST("v1/apps/{id}/mod/recommend")
    suspend fun proposeMod(@Path("id") appId: Int, @Body propose: ProposeDto): Response<Unit>

}