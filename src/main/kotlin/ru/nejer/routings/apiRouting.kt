package ru.nejer.routings

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.nejer.models.KingdomDTO
import ru.nejer.models.Kingdoms

fun Route.apiRoute() {
    route("/api") {
        get("/all") {
            call.respond(
                transaction {
                    Kingdoms.selectAll().map {
                        KingdomDTO.mapToKingdomDTO(it)
                    }
                }
            ) //todo
        }
    }
}