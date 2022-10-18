package ru.nejer.models.tables

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Points : Table() {
    val id = integer("id").autoIncrement()
    val longitude = double("longitude")
    val latitude = double("latitude")
    val polygon3Id = reference("polygon3Id", Polygons3.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(id)
}