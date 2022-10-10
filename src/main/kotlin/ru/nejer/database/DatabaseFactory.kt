package ru.nejer.database

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import ru.nejer.models.*

object DatabaseFactory {
    fun init() {
        val driverClassName = "org.h2.Driver"
        val jdbcURL = "jdbc:h2:file:./build/db"
        val database = Database.connect(jdbcURL, driverClassName)
        transaction(database) {
            SchemaUtils.drop(Organisms, OrganismRegion)
            SchemaUtils.create(Kingdoms)
            SchemaUtils.create(Organisms)
            SchemaUtils.create(Regions)
            SchemaUtils.create(OrganismRegion)

//            val geoJsonModel = Json.decodeFromString<GeoJsonModel>(Constants.geojson)
//
//            Regions.batchInsert(geoJsonModel.features) {
//                this[Regions.name] = it.properties.local_name
//            }

            Organisms.insert {
                it[nameLatin] = "Helvella lacunosa Afzel."
                it[nameRussian] = "Гельвелла ямчатая (г. бороздчатая, лопастник ямчатый)"
                it[kingdomId] = 2
            }

            Organisms.insert {
                it[nameLatin] = "Cortinarius violaceus (L.) Gray"
                it[nameRussian] = "Паутинник фиолетовый"
                it[kingdomId] = 2
            }

            Organisms.insert {
                it[nameRussian] = "Батрахоспермум слизистый, или чётковидный"
                it[nameLatin] = "Batrachospermum gelatinosum (Linnaeus) De Candolle"
                it[kingdomId] = 1
            }

            OrganismRegion.insert {
                it[regionId] = 5
                it[organismId] = 3
                it[count] = 3
            }
        }
    }
}