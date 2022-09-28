package ru.nejer.routings.v1

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.nejer.models.KingdomDTO
import ru.nejer.models.Kingdoms
import ru.nejer.models.Organisms

fun Route.v1() {
    route("/v1") {
        route("/kingdoms") {
            get {
                call.respond(transaction {
                    Kingdoms.selectAll().map {
                        KingdomDTO.mapToKingdomDTO(it)
                    }
                })
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toInt() ?: return@get call.respondText("id не найден")
                call.respond(
                    transaction {
                        KingdomDTO.mapToKingdomDTO(Kingdoms.select { Kingdoms.id eq id }.first())
                    }
                )
            }
        }
    }
}