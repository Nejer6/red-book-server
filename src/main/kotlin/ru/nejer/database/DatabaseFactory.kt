package ru.nejer.database

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.nejer.json.JsonParser
import ru.nejer.models.tables.*

object DatabaseFactory {
    fun init() {
        val driverClassName = "org.h2.Driver"
        val jdbcURL = "jdbc:h2:file:./build/db"
        val database = Database.connect(jdbcURL, driverClassName)
        transaction(database) {
            SchemaUtils.create(Animals, Polygons1, Polygons2, Polygons3, Points, Kingdoms)

            //insertAnimalsGeojson(kingdomId)
        }
    }

    private fun insertAnimalsGeojson(_kingdomId: Int) {
        val animalsGeoJson = JsonParser.animals

        animalsGeoJson.features.forEach { animal ->
            val newAnimalId = Animals.insert {
                it[name] = animal.properties.name
                it[nameRu] = animal.properties.nameRu
                it[rare] = animal.properties.rare
                it[kingdomId] = _kingdomId
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