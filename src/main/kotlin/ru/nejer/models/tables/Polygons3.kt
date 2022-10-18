package ru.nejer.models.tables

import org.jetbrains.exposed.sql.Table

object Polygons3 : Table() {
    val id = integer("id").autoIncrement()
    val polygon2Id = reference("polygon2Id", Polygons2.id)

    override val primaryKey = PrimaryKey(id)
}