package ru.nejer.routings.v1

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.nejer.json.JsonParser
import ru.nejer.models.*
import java.io.File

fun Route.v1() {
    route("/v1") {
        route("/organisms") {
            get {
                val organismDtos = transaction {
                    val organismsQuery = Organisms
                        .innerJoin(OrganismRegion)
                        .innerJoin(Regions)
                        .slice(
                            Organisms.id,
                            Organisms.nameRussian,
                            Organisms.nameLatin,
                            Organisms.kingdomId,
                            OrganismRegion.count,
                            Regions.name
                        )
                        .selectAll()


                    call.request.queryParameters["search"]?.uppercase()?.let {
                        organismsQuery.andWhere {
                            (Organisms.nameRussian.upperCase() like "%$it%") or
                                    (Organisms.nameLatin.upperCase() like "%$it%")
                        }
                    }

                    mapToOrganismDto(organismsQuery)
                }

                call.respond(organismDtos)
            }
        }

        route("/regions") {
            get {
                val regionDtos = transaction {
                    val regionsQuery = Regions.selectAll()

                    mapToRegionsDTO(regionsQuery)
                }

                call.respond(regionDtos)
            }
        }

        route("/regions-geojson") {
            get {
                call.respond(JsonParser.regions)
            }
        }

        route("/kingdoms") {
            get {
                val kingdomDtos = transaction {
                    val kingdomsQuery = Kingdoms.selectAll()

                    mapToKingdomDTO(kingdomsQuery)
                }

                call.respond(kingdomDtos)
            }
        }
    }
}
