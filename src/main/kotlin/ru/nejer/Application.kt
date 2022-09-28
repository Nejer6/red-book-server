package ru.nejer

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.nejer.database.DatabaseFactory
import ru.nejer.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        DatabaseFactory.init()
        configureSerialization()
        configureTemplating()
        configureRouting()
    }.start(wait = true)
}
