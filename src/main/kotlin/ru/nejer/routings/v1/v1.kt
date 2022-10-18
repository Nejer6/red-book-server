package ru.nejer.routings.v1

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.transactions.transaction
import ru.nejer.json.JsonParser
import ru.nejer.models.*
import ru.nejer.models.tables.*
import java.io.File

fun Route.v1() {
    route("/v1") {
        route("/organisms") {
            get {
                val organismDtos = transaction {
                    val organismsQuery = Organisms
                        .innerJoin(OrganismRegion)
                        .innerJoin(Regions)
                        .innerJoin(Kingdoms)
                        .slice(
                            Organisms.id,
                            Organisms.nameRussian,
                            Organisms.nameLatin,
                            Organisms.kingdomId,
                            OrganismRegion.count,
                            Regions.name,
                            Kingdoms.nameRussian,
                            Kingdoms.nameLatin
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

        route("/animals-geojson") {
            get {
                call.respond(JsonParser.animals)
            }
        }

        route("/animals") {
            get {
                val animalsDtos = transaction {
                    val animalsQuery = Animals.selectAll()

                    call.request.queryParameters["search"]?.uppercase()?.let {
                        animalsQuery.andWhere {
                            (Animals.nameRu.upperCase() like "%$it%") or
                                    (Animals.name.upperCase() like "%$it%")
                        }
                    }

                    call.request.queryParameters["offset"]?.toLong()?.let { offset ->
                        call.request.queryParameters["count"]?.toInt()?.let { count ->
                            animalsQuery.limit(count, offset)
                        }
                    }

                    animalsQuery.map {
                        Animal(
                            id = it[Animals.id],
                            name = it[Animals.name],
                            nameRu = it[Animals.nameRu],
                            rare = it[Animals.rare]
                        )
                    }

                    //mapToFeature(animalsQuery)
                }
                call.respond(animalsDtos)
            }

            route("/{id}") {
                get {
                    val id = call.parameters.getOrFail<Int>("id").toInt()
                    val animalDtos = transaction {
                        val animalQuery = Animals
                            .innerJoin(Polygons1)
                            .innerJoin(Polygons2)
                            .innerJoin(Polygons3)
                            .innerJoin(Points)
                            .select { Animals.id eq id }

                        toFeature(animalQuery.toList())
                    }

                    call.respond(animalDtos)
                }
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
