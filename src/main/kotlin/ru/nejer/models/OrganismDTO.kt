package ru.nejer.models

import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

@kotlinx.serialization.Serializable
data class OrganismDTO(
    val id: Int,
    val nameRussian: String,
    val nameLatin: String,
    val kingdomId: Int,
    val region: MutableMap<String, Int>
) {
    companion object {
        fun mapToOrganismDTO(it: ResultRow) = OrganismDTO(
            id = it[Organisms.id],
            nameRussian = it[Organisms.nameRussian],
            nameLatin = it[Organisms.nameLatin],
            kingdomId = it[Organisms.kingdomId],
            region = mutableMapOf()
//            region = mutableMapOf(
//                "Мурманская область" to it[Organisms.Murmansk],
//                "Ямало-ненецкий автономный округ" to it[Organisms.YamaloNenets],
//                "Республика Карелия" to it[Organisms.Karelia],
//                "Архангельская область" to it[Organisms.Arkhangelsk],
//                "Республика Коми" to it[Organisms.Komi],
//                "Ненецкий автономный округ" to it[Organisms.Nenets],
//                "Красноярский край" to it[Organisms.Krasnoyarsk],
//                "Республика Саха (Якутия)" to it[Organisms.Yakutia],
//                "Чукотский автономный округ" to it[Organisms.Chukotka]
//            ).filter {
//                it.value != null
//            }

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
            kingdomId = firstRow[Organisms.kingdomId],
            region = mutableMapOf()
        )

        it.forEach { resultRow ->
            organismDTO.region[resultRow[Regions.name]] = resultRow[OrganismRegion.count]
        }
        organismDTO
    }
