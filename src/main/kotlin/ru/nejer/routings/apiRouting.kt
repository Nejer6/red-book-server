package ru.nejer.routings

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import ru.nejer.models.Kingdoms

fun Route.apiRoute() {
    route("/api") {
        get("/all") {
            call.respondText(
                transaction {
                    Kingdoms.select{ Kingdoms.id eq 1 }.first()[Kingdoms.nameRussian]
                }
            ) //todo
        }
    }
}