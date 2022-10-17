package ru.nejer.models

import org.jetbrains.exposed.sql.Table

object Dots : Table() {
    private val id = integer("id").autoIncrement()
    val organismId = reference("organismId", Organisms.id)
    val latitude = double("latitude")
    val longitude = double("longitude")

    override val primaryKey = PrimaryKey(id)
}
