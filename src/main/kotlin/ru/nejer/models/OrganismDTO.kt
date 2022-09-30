package ru.nejer.models

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

@kotlinx.serialization.Serializable
data class OrganismDTO(val id: Int, val nameRussian: String, val nameLatin: String, val region: Map<String, Int?>) {
    companion object {
        fun mapToOrganismDTO(it: ResultRow) = OrganismDTO(
            id = it[Organisms.id],
            nameRussian = it[Organisms.nameRussian],
            nameLatin = it[Organisms.nameLatin],
            region = mapOf(
                "Мурманская область" to it[Organisms.Murmansk],
                "Ямало-ненецкий автономный округ" to it[Organisms.YamaloNenets],
                "Республика Карелия" to it[Organisms.Karelia],
                "Архангельская область" to it[Organisms.Arkhangelsk],
                "Республика Коми" to it[Organisms.Komi],
                "Ненецкий автономный округ" to it[Organisms.Nenets],
                "Красноярский край" to it[Organisms.Krasnoyarsk],
                "Республика Саха (Якутия)" to it[Organisms.Yakutia],
                "Чукотский автономный округ" to it[Organisms.Chukotka]
            ).filter {
                it.value != null
            }
        )
    }
}

object Organisms : Table() {
    val id = integer("id").autoIncrement()
    val nameRussian = varchar("nameRussian", 100)
    val nameLatin = varchar("nameLatin", 100)
    val kingdom = reference("kingdom", Kingdoms.id)
    val Murmansk = integer("Murmansk").nullable()
    val YamaloNenets = integer("YamaloNenets").nullable()
    val Karelia = integer("Karelia").nullable()
    val Arkhangelsk = integer("Arkhangelsk").nullable()
    val Komi = integer("Komi").nullable()
    val Nenets = integer("Nenets").nullable()
    val Krasnoyarsk = integer("Krasnoyarsk").nullable()
    val Yakutia = integer("Yakutia").nullable()
    val Chukotka= integer("Chukotka").nullable()

    override val primaryKey = PrimaryKey(id)
}
