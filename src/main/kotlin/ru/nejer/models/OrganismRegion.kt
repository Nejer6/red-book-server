package ru.nejer.models

import org.jetbrains.exposed.sql.Table

object OrganismRegion : Table() {
    val id = integer("id").autoIncrement()
    val organismId = reference("organismId", Organisms.id)
    val regionId = reference("regionId", Regions.id)
    val count = integer("count")

    override val primaryKey = PrimaryKey(id)
}
