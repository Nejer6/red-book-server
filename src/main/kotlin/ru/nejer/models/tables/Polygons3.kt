package ru.nejer.models.tables

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Polygons3 : Table() {
    val id = integer("id").autoIncrement()
    val polygon2Id = reference("polygon2Id", Polygons2.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(id)
}