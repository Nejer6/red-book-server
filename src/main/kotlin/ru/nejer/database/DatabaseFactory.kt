package ru.nejer.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import ru.nejer.models.Kingdoms
import ru.nejer.models.Organisms

object DatabaseFactory {
    fun init() {
        val driverClassName = "org.h2.Driver"
        val jdbcURL = "jdbc:h2:file:./build/db"
        val database = Database.connect(jdbcURL, driverClassName)
        transaction(database) {
            SchemaUtils.drop(Kingdoms, Organisms)
            SchemaUtils.create(Kingdoms)
            SchemaUtils.create(Organisms)

            val fungiId= Kingdoms.insert {
                it[nameRussian] = "Грибы"
                it[nameLatin] = "Fungi"
            } get Kingdoms.id

            val plantaeId = Kingdoms.insert {
                it[nameRussian] = "Растения"
                it[nameLatin] = "Plantae"
            } get Kingdoms.id

            Organisms.insert {
                it[nameLatin] = "Helvella lacunosa Afzel."
                it[nameRussian] = "Гельвелла ямчатая (г. бороздчатая, лопастник ямчатый)"
                it[kingdom] = fungiId
                it[Nenets] = 3
            }

            Organisms.insert {
                it[nameLatin] = "Cortinarius violaceus (L.) Gray"
                it[nameRussian] = "Паутинник фиолетовый"
                it[kingdom] = fungiId
                it[Murmansk] = 3
                it[Karelia] = 3
                it[Nenets] = 3
            }

            Organisms.insert {
                it[nameRussian] = "Гельвелла ямчатая (г. бороздчатая, лопастник ямчатый)"
                it[nameLatin] = "Helvella lacunosa Afzel."
                it[kingdom] = plantaeId
                it[Nenets] = 3
            }
        }
    }
}