package ru.nejer.routings.v1

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.nejer.models.*
import ru.nejer.models.tables.*
import java.io.File

fun Route.v1() {
    route("/v1") {
        route("/animals") {
            get {
                val animalsDtos = transaction {
                    val animalsQuery = Animals.innerJoin(Kingdoms).innerJoin(Regions).leftJoin(AnimalClass).selectAll()

                    call.request.queryParameters["region"]?.let {
                        animalsQuery.andWhere { Regions.nameRu eq it }
                    }

                    call.request.queryParameters["kingdom"]?.let {
                        animalsQuery.andWhere { Kingdoms.name eq it }
                    }

                    call.request.queryParameters["animalClass"]?.let {
                        animalsQuery.andWhere { AnimalClass.name eq it }
                    }

                    call.request.queryParameters["search"]?.uppercase()?.let {
                        animalsQuery.andWhere {
                            (Animals.nameRu.upperCase() like "%$it%") or
                                    (Animals.name.upperCase() like "%$it%")
                        }
                    }

                    call.request.queryParameters["sort"]?.let {
                        val sortOrder = if (it == "ASC") {
                            SortOrder.ASC
                        } else {
                            SortOrder.DESC
                        }

                        animalsQuery.orderBy(Animals.rare to sortOrder, Animals.id to SortOrder.ASC)
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
                            kingdom = it[Kingdoms.name],
                            region = it[Regions.nameRu],
                            animalClass = it[AnimalClass.name]
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
                            .innerJoin(Regions)
                            .leftJoin(AnimalClass)
                            .select { Animals.id eq id }

                        toFeature(animalQuery.toList())
                    }

                    call.respond(animalDtos)
                }
            }
        }

        route("/arctic-territory") {
            get {
                call.respond(File("json/arctic_territory.geojson").readText(Charsets.UTF_8))
            }
        }
    }
}
