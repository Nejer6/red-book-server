package ru.nejer.routings

import io.ktor.server.routing.*
import ru.nejer.routings.v1.v1

fun Route.apiRoute() {
    route("/api") {
        v1()
    }
}