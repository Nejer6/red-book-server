package ru.nejer.models.tables

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Polygons2 : Table() {
    val id = integer("id").autoIncrement()
    val polygon1Id = reference("polygon1Id", Polygons1.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(id)
}