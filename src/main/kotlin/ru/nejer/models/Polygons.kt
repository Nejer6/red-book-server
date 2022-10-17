package ru.nejer.models

import org.jetbrains.exposed.sql.Table

object Polygons : Table() {
    val id = integer("id").autoIncrement()
    val organismId = reference("organismId", Organisms.id)

    override val primaryKey = PrimaryKey(id)
}