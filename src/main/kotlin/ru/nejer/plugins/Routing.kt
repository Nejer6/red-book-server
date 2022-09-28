package ru.nejer.plugins

import io.ktor.server.routing.*
import io.ktor.server.http.content.*
import io.ktor.server.application.*
import ru.nejer.routings.apiRoute
import java.io.File

fun Application.configureRouting() {

    routing {
        static {
            staticRootFolder = File("files")
            default("index.html")
            files(".")
        }
        apiRoute()
    }
}
