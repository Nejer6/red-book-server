package ru.nejer.models.tables

import org.jetbrains.exposed.sql.Table

object Polygons1 : Table() {
    val id = integer("id").autoIncrement()
    val animalId = reference("animalId", Animals.id)

    override val primaryKey = PrimaryKey(id)
}