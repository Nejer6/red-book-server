package ru.nejer.models

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

@kotlinx.serialization.Serializable
data class OrganismDTO(val id: Int, val nameRussian: String, val nameLatin: String, val kingdom: Int) {
    companion object {
        fun mapToOrganismDTO(it: ResultRow) = OrganismDTO(
            id = it[Organisms.id],
            nameRussian = it[Organisms.nameRussian],
            nameLatin = it[Organisms.nameLatin],
            kingdom = it[Organisms.kingdom]
        )
    }
}

object Organisms : Table() {
    val id = integer("id").autoIncrement()
    val nameRussian = varchar("nameRussian", 100)
    val nameLatin = varchar("nameLatin", 100)
    val kingdom = reference("kingdom", Kingdoms.id)

    override val primaryKey = PrimaryKey(id)
}
