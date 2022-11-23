package ru.nejer.database

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.nejer.models.Animal
import ru.nejer.models.GeoJsonModel
import ru.nejer.models.tables.*
import java.io.File

object DatabaseFactory {
    fun init() {
        val driverClassName = "org.h2.Driver"
        //val jdbcURL = "jdbc:h2:file:./build/db"
        val jdbcURL = "jdbc:h2:file:./data/db"
        val database = Database.connect(jdbcURL, driverClassName)
        transaction(database) {
            SchemaUtils.create(
                Polygons1,
                Polygons2,
                Polygons3,
                Points,
                Kingdoms,
                Regions,
                Animals,
                AnimalClass
            )

            //Animals.deleteWhere { Animals.kingdomId eq 3 }

            //SchemaUtils.create(Polygons1, Polygons2, Polygons3, Points, Kingdoms, Regions, Animals, AnimalClass)

            //Animals.deleteWhere { Animals.kingdomId eq 1 }

            //insertAnimalsGeojson(3, "json/plants.geojson")
        }
    }

    private fun insertAnimalClass(name: String): Int {
        return AnimalClass.insert {
            it[this.name] = name
        } get AnimalClass.id
    }

    private fun insertAnimalsGeojson(_kingdomId: Int, pathname: String) {
        val animalsGeoJson =
            Json.decodeFromString<GeoJsonModel>(File(pathname).readText(Charsets.UTF_8))

        animalsGeoJson.features.forEach { animal ->
            val newAnimalId = Animals.insert {
                it[name] = animal.properties.name
                it[nameRu] = animal.properties.nameRu
                it[rare] = animal.properties.rare
                it[kingdomId] = _kingdomId
                it[regionId] = Regions.select { Regions.nameRu eq animal.properties.adm_name }.first()[Regions.id]

                if (animal.properties.animalClass != null) {
                    val animalClassRow = AnimalClass.select { AnimalClass.name eq animal.properties.animalClass }.firstOrNull()
                    if (animalClassRow == null) {
                        it[animalClass] = insertAnimalClass(animal.properties.animalClass)
                    } else {
                        it[animalClass] = animalClassRow[AnimalClass.id]
                    }
                }

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