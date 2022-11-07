package ru.nejer.models.tables

import org.jetbrains.exposed.sql.Table

object Regions : Table() {
    val id = integer("id").autoIncrement()
    val nameRu = varchar("nameRu", 31)
    val name = varchar("name", 30)

    override val primaryKey = PrimaryKey(id)
}