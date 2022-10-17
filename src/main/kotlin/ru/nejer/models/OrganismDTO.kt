package ru.nejer.models

import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

@kotlinx.serialization.Serializable
data class OrganismDTO(
    val id: Int,
    val nameRussian: String,
    val nameLatin: String,
    val kingdomRussian: String,
    val kingdomLatin: String,
    val region: MutableMap<String, Int>
) {
    companion object {
        fun mapToOrganismDTO(it: ResultRow) = OrganismDTO(
            id = it[Organisms.id],
            nameRussian = it[Organisms.nameRussian],
            nameLatin = it[Organisms.nameLatin],
            kingdomRussian = it[Kingdoms.nameRussian],
            kingdomLatin = it[Kingdoms.nameLatin],
            region = mutableMapOf()
        )
    }
}

object Organisms : Table() {
    val id = integer("id").autoIncrement()
    val nameRussian = varchar("nameRussian", 100)
    val nameLatin = varchar("nameLatin", 100)
    val kingdomId = reference("kingdomId", Kingdoms.id)

    override val primaryKey = PrimaryKey(id)
}

fun mapToOrganismDto(organismQuery: Query) = organismQuery
    .groupBy { it[Organisms.id] }
    .values
    .map {
        val firstRow = it.first()
        val organismDTO = OrganismDTO(
            id = firstRow[Organisms.id],
            nameRussian = firstRow[Organisms.nameRussian],
            nameLatin = firstRow[Organisms.nameLatin],
            kingdomRussian = firstRow[Kingdoms.nameRussian],
            kingdomLatin = firstRow[Kingdoms.nameLatin],
            region = mutableMapOf()
        )

        it.forEach { resultRow ->
            organismDTO.region[resultRow[Regions.name]] = resultRow[OrganismRegion.count]
        }
        organismDTO
    }
