package ru.nejer.routings.v1

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.nejer.models.*
import ru.nejer.models.tables.*

fun Route.v1() {
    route("/v1") {
        route("/animals") {
            get {
                val animalsDtos = transaction {
                    val animalsQuery = Animals.innerJoin(Kingdoms).selectAll()

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
                            rare = it[Animals.rare],
                            kingdom = it[Kingdoms.name]
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
    }
}
