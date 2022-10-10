package ru.nejer

import io.ktor.server.application.*
import io.ktor.server.plugins.cors.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.serialization.json.Json
import ru.nejer.database.DatabaseFactory
import ru.nejer.models.GeoJsonModel
import ru.nejer.plugins.*
import kotlinx.serialization.decodeFromString

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        install(CORS) {
            anyHost()
        }
        DatabaseFactory.init()
        configureSerialization()
        configureTemplating()
        configureRouting()
    }.start(wait = true)
}
