package ru.nejer.models

import org.jetbrains.exposed.dao.id.IntIdTable

data class Kingdom(val id: Int, val nameRussian: String, val nameLatin: String)

object Kingdoms : IntIdTable() {
    val nameRussian = varchar("nameRussian", 50)
    val nameLatin = varchar("nameLatin", 50)
}