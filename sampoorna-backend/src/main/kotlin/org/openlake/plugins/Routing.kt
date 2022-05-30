package org.openlake.plugins

import io.ktor.server.routing.*
import io.ktor.server.application.*
import org.openlake.authenticate
import org.openlake.data.user.UserDataSource
import org.openlake.getSecretInfo
import org.openlake.security.hashing.HashingService
import org.openlake.security.token.TokenConfig
import org.openlake.security.token.TokenService
import org.openlake.signIn
import org.openlake.signUp

fun Application.configureRouting(
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {

    routing {
        signIn(userDataSource, hashingService, tokenService, tokenConfig)
        signUp(hashingService,userDataSource)
        authenticate()
        getSecretInfo()
    }
}
