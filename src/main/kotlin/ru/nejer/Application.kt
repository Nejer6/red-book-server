package ru.nejer

import io.ktor.server.application.*
import io.ktor.server.plugins.cors.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.nejer.database.DatabaseFactory
import ru.nejer.plugins.*


fun main() {
    embeddedServer(Netty, port = 9091, module = Application::myApplicationModule, host = "192.168.0.107").start(wait = true)
}

fun Application.myApplicationModule() {
    install(CORS) {
        anyHost()
    }
    DatabaseFactory.init()
    configureSerialization()
    configureTemplating()
    configureRouting()
}
