package ru.nejer.routings.v1

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.nejer.models.KingdomDTO
import ru.nejer.models.Kingdoms
import ru.nejer.models.OrganismDTO
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

            route("/{kingdomId}") {
                get {
                    val id = call.parameters["kingdomId"]?.toInt() ?: return@get call.respondText("kingdom not found")
                    call.respond(
                        transaction {
                            KingdomDTO.mapToKingdomDTO(Kingdoms.select { Kingdoms.id eq id }.first())
                        }
                    )
                }

                route("/organisms") {
                    get {
                        val id =
                            call.parameters["kingdomId"]?.toInt() ?: return@get call.respondText("kingdom not found")
                        call.respond(
                            transaction {
                                Organisms.select { Organisms.kingdom eq id }.map {
                                    OrganismDTO.mapToOrganismDTO(it)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}