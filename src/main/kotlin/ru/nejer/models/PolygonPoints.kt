package ru.nejer.models

import org.jetbrains.exposed.sql.Table

object PolygonPoints : Table() {
    val id = integer("id").autoIncrement()
    val polygonId = reference("polygonId", Polygons.id)
    val latitude = double("latitude")
    val longitude = double("longitude")

    override val primaryKey = PrimaryKey(id)
}