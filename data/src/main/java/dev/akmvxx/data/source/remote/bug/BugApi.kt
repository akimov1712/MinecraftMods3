package dev.akmvxx.data.source.remote.bug

import dev.akmvxx.data.source.remote.propose.ProposeDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

internal interface BugApi {

    @POST("v1/apps/{id}/issue")
    suspend fun reportBug(@Path("id") appId: Int, @Body bug: BugDto): Response<Unit>

}
