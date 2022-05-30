package org.openlake

import io.ktor.server.application.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import org.openlake.data.user.MongoUserDataSource
import org.openlake.plugins.*
import org.openlake.security.hashing.SHA256HashingService
import org.openlake.security.token.JwtTokenService
import org.openlake.security.token.TokenConfig

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    val mongoPw = System.getenv("MONGO_PW")
    val db = KMongo.createClient(
        connectionString = "mongodb+srv://yellowhatpro:$mongoPw@cluster0.ncotj.mongodb.net/todo?retryWrites=true&w=majority"
    ).coroutine
        .getDatabase("todo")
    val userDataSource = MongoUserDataSource(db)
    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        expiresIn = 365L * 1000L* 60L * 60L * 24L,
        audience = environment.config.property("jwt.audience").getString(),
        secret = System.getenv("JWT_SECRET")
    )
    val hashingService = SHA256HashingService()
    configureRouting(userDataSource, hashingService, tokenService, tokenConfig)
    configureSecurity(config = tokenConfig)
    configureMonitoring()
    configureSerialization()
}
