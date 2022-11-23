package ru.nejer.models.tables

import org.jetbrains.exposed.sql.Table

object AnimalClass : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50).nullable()

    override val primaryKey = PrimaryKey(id)
}