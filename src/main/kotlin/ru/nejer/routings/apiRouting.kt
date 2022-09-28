package ru.nejer.routings

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

fun Route.apiRoute() {
    route("/api") {
        get("/organisms") {
            call.respond(
                transaction {
                    Organisms.selectAll().map {
                        OrganismDTO.mapToOrganismDTO(it)
                    }
                }
            )
        }

        get("/kingdoms") {
            call.respond(
                transaction {
                    Kingdoms.selectAll().map {
                        KingdomDTO.mapToKingdomDTO(it)
                    }
                }
            )
        }

        get("/org") {
            call.respond(
                transaction {
                    (Organisms innerJoin Kingdoms).slice(Organisms.kingdom, Organisms.nameRussian, Kingdoms.nameRussian)
                        .selectAll().map {
                        "${it[Organisms.nameRussian]} живет в ${it[Kingdoms.nameRussian]}"
                    }
                }
            )
        }
    }
}