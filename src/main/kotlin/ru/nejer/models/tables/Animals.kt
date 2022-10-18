package ru.nejer.models.tables

import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import ru.nejer.models.*

object Animals : Table() {
    val id = integer("id").autoIncrement()
    val nameRu = varchar("nameRu", 100)
    val name = varchar("name", 100).nullable()
    val kingdomId = reference("kingdomId", Kingdoms.id, onDelete = ReferenceOption.CASCADE)
    val rare = varchar("rare", 3)

    override val primaryKey = PrimaryKey(id)
}

fun mapToFeature(featureQuery: Query) = featureQuery
    .groupBy { resultRow ->
        resultRow[Animals.id]
    }
    .values
    .map { animalResultRows ->
        toFeature(animalResultRows)
    }

fun toFeature(animalResultRows: List<ResultRow>): Feature {
    val firstRow = animalResultRows.first()

    return Feature(
        properties = Properties(
            id = firstRow[Animals.id],
            nameRu = firstRow[Animals.nameRu],
            name = firstRow[Animals.name],
            rare = firstRow[Animals.rare]
        ),
        geometry = Geometry(
            coordinates = animalResultRows
                .groupBy { it[Polygons2.id] }
                .values
                .map {
                    it.groupBy { it[Polygons3.id] }
                        .values
                        .map {
                            it.groupBy { it[Points.id] }
                                .values
                                .map {
                                    listOf(it.first()[Points.longitude], it.first()[Points.latitude])
                                }
                        }
                }
        )
    )
}