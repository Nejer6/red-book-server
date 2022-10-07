package ru.nejer.routings.v1

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.nejer.models.KingdomDTO
import ru.nejer.models.Kingdoms
import ru.nejer.models.OrganismDTO
import ru.nejer.models.Organisms

fun Route.v1() {
    route("/v1") {
        route("/organisms") {
            get {
                val offset = call.request.queryParameters["offset"]?.toInt()
                val count = call.request.queryParameters["count"]?.toInt()
                val search = call.request.queryParameters["search"] ?: ""
                println(search)

                var array = transaction {

                    Organisms.select {
                        (Organisms.nameRussian regexp search) or (Organisms.nameLatin regexp search)
                    }.map {
                        OrganismDTO.mapToOrganismDTO(it)
                    }

                }

                if (count != null && offset != null) {
                    val start = if (offset >= array.size) {
                        return@get call.respondText("Not found")
                    } else {
                        offset
                    }

                    var end = offset + count
                    if (end > array.size) end = array.size

                    array = array.slice(start until end)
                }

                call.respond(array)
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
