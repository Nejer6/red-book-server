package ru.nejer.database

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.nejer.json.JsonParser
import ru.nejer.models.*
import ru.nejer.models.tables.*

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

            SchemaUtils.create(Animals, Polygons1, Polygons2, Polygons3, Points)

//            insertAnimalsGeojson()
        }
    }

    private fun insertAnimalsGeojson() {
        val animalsGeoJson = JsonParser.animals

        animalsGeoJson.features.forEach { animal ->
            val newAnimalId = Animals.insert {
                it[name] = animal.properties.name
                it[nameRu] = animal.properties.nameRu
                it[rare] = animal.properties.rare
            } get Animals.id

            val newPolygon1Id = Polygons1.insert {
                it[animalId] = newAnimalId
            } get Polygons1.id

            animal.geometry.coordinates.forEach {
                val newPolygon2Id = Polygons2.insert {
                    it[polygon1Id] = newPolygon1Id
                } get Polygons2.id

                it.forEach {
                    val newPolygon3Id = Polygons3.insert {
                        it[polygon2Id] = newPolygon2Id
                    } get Polygons3.id

                    it.forEach { point ->
                        Points.insert {
                            it[longitude] = point.first()
                            it[latitude] = point.last()
                            it[polygon3Id] = newPolygon3Id
                        }
                    }
                }
            }
        }
    }
}