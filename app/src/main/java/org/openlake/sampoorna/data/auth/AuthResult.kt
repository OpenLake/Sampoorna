package org.openlake.sampoorna.data.auth

sealed class AuthResult{
    class Authorized : AuthResult()
    class Unauthorized : AuthResult()
    class UnknownError : AuthResult()
}
