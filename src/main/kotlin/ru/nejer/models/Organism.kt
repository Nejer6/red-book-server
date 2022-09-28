package ru.nejer.models

import org.jetbrains.exposed.dao.id.IntIdTable

data class Organism(val id: Int, val nameRussian: String, val nameLatin: String, val kingdom: String)

object Organisms : IntIdTable() {
    val nameRussian = varchar("nameRussian", 100)
    val nameLatin = varchar("nameLatin", 100)
    val kingdom = reference("kingdom", Kingdoms)
}
