package ru.nejer.routings.v1

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import ru.nejer.models.OrganismDTO
import ru.nejer.models.Organisms

fun Route.v1() {
    route("/v1") {
        route("/organisms") {
            get {
                val offset = call.request.queryParameters["offset"]?.toLong()
                val count = call.request.queryParameters["count"]?.toInt()
                val search = call.request.queryParameters["search"]?.let {
                    "%$it%"
                } ?: "%"

                call.respond(
                    transaction {
                        Organisms.select {
                            (Organisms.nameRussian like search) or (Organisms.nameLatin like search)
                        }.let {
                            if (count != null && offset != null) {
                                it.limit(count, offset)
                            } else {
                                it
                            }
                        }.map {
                            OrganismDTO.mapToOrganismDTO(it)
                        }
                    }
                )
            }
        }

//        route("/kingdoms") {
//            get {
//                call.respond(transaction {
//                    Kingdoms.selectAll().map {
//                        KingdomDTO.mapToKingdomDTO(it)
//                    }
//                })
//            }
//
//            route("/{kingdomId}") {
//                get {
//                    val id =
//                        call.parameters["kingdomId"]?.toInt() ?: return@get call.respondText("kingdom not found")
//                    call.respond(
//                        transaction {
//                            KingdomDTO.mapToKingdomDTO(Kingdoms.select { Kingdoms.id eq id }.first())
//                        }
//                    )
//                }
//
//                route("/organisms") {
//                    get {
//                        val offset = call.request.queryParameters["offset"]?.toInt()
//                        val count = call.request.queryParameters["count"]?.toInt()
//
//                        val id =
//                            call.parameters["kingdomId"]?.toInt()
//                                ?: return@get call.respondText("kingdom not found")
//                        var array = transaction {
//                            Organisms.select { Organisms.kingdom eq id }.map {
//                                OrganismDTO.mapToOrganismDTO(it)
//                            }
//                        }
//
//                        if (count != null && offset != null) {
//                            val start = if (offset >= array.size) {
//                                return@get call.respondText("Not found")
//                            } else {
//                                offset
//                            }
//
//                            var end = offset + count
//                            if (end > array.size) end = array.size
//
//                            array = array.slice(start until end)
//                        }
//
//                        call.respond(array)
//
//
//                    }
//                }
//            }
//        }
    }
}
