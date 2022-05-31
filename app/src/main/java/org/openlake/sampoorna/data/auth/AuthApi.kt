package org.openlake.sampoorna.data.auth

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    // TODO
    @POST("")
    suspend fun signUp(
        @Body
        request : AuthRequest
    )

    @POST("")
    suspend fun signIn(
        @Body
        request: AuthRequest
    ) : TokenResponse

    @GET("")
    suspend fun authenticate(
        @Header("Authorization")
        token : String
    )
}