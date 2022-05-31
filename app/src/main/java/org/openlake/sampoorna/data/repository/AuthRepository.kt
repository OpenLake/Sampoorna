package org.openlake.sampoorna.data.repository

import org.openlake.sampoorna.data.auth.AuthResult

interface AuthRepository {
    suspend fun signIn(username : String,password : String) : AuthResult
    suspend fun signUp(username: String,password: String) : AuthResult
    suspend fun authenticate() : AuthResult
}