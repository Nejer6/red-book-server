package ru.nejer.models

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Query

@Serializable
data class KingdomDTO(val id: Int, val nameRussian: String, val nameLatin: String) {
    companion object {
        fun mapToKingdomDTO(it: ResultRow) = KingdomDTO(
            id = it[Kingdoms.id],
            nameRussian = it[Kingdoms.nameRussian],
            nameLatin = it[Kingdoms.nameLatin]
        )
    }
}

object Kingdoms : Table() {
    val id = integer("id").autoIncrement()
    val nameRussian = varchar("nameRussian", 50)
    val nameLatin = varchar("nameLatin", 50)

    override val primaryKey = PrimaryKey(id)
}

fun mapToKingdomDTO(kingdomQuery: Query) = kingdomQuery
    .map {
        KingdomDTO(
            id = it[Kingdoms.id],
            nameLatin = it[Kingdoms.nameLatin],
            nameRussian = it[Kingdoms.nameRussian]
        )
    }