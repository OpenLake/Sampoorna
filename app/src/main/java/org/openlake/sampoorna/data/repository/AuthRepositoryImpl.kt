package org.openlake.sampoorna.data.repository

import android.content.SharedPreferences
import org.openlake.sampoorna.data.auth.AuthApi
import org.openlake.sampoorna.data.auth.AuthRequest
import org.openlake.sampoorna.data.auth.AuthResult
import retrofit2.HttpException

class AuthRepositoryImpl(
    val api : AuthApi,
    val preferences : SharedPreferences
    ) : AuthRepository {
    override suspend fun signIn(username: String, password: String): AuthResult {
        return try{
            val response = api.signIn(AuthRequest(username,password))
            preferences.edit()
                .putString("jwtToken",response.token)
                .apply()
            AuthResult.Authorized()
        }catch (e : HttpException){
            when(e.code()){
                401 -> AuthResult.Unauthorized()
                else -> AuthResult.UnknownError()
            }
        }catch(e : Exception){
            AuthResult.UnknownError()
        }
    }

    override suspend fun signUp(username: String, password: String): AuthResult {
        return try{
            api.signUp(AuthRequest(username,password))
            signIn(username,password)
        }catch (e : HttpException){
            when(e.code()){
                401 -> AuthResult.Unauthorized()
                else -> AuthResult.UnknownError()
            }
        }catch(e : Exception){
            AuthResult.UnknownError()
        }
    }

    override suspend fun authenticate(): AuthResult {
        val token = preferences.getString("jwtToken",null) ?: return AuthResult.Unauthorized()
        return try{
            api.authenticate(token)
            AuthResult.Authorized()
        }catch (e : HttpException){
            when(e.code()){
                401 -> AuthResult.Unauthorized()
                else -> AuthResult.UnknownError()
            }
        }catch(e : Exception){
            AuthResult.UnknownError()
        }
    }
}