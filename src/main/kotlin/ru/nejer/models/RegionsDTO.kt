package ru.nejer.models

import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.Table

@kotlinx.serialization.Serializable
data class RegionsDTO(
    val id: Int,
    val name: String
)

object Regions : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)

    override val primaryKey = PrimaryKey(id)
}

fun mapToRegionsDTO(regionsQuery: Query) = regionsQuery
    .map {
        RegionsDTO(
            id = it[Regions.id],
            name = it[Regions.name]
        )
    }
