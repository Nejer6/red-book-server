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

            val gribyId= Kingdoms.insert {
                it[nameRussian] = "Грибы"
                it[nameLatin] = "Griby"
            } get Kingdoms.id

            val jivotnieId = Kingdoms.insert {
                it[nameRussian] = "Животные"
                it[nameLatin] = "Jivotnie"
            } get Kingdoms.id

            Organisms.insert {
                it[nameLatin] = "Horse"
                it[nameRussian] = "Лошадь"
                it[kingdom] = jivotnieId
            }

            Organisms.insert {
                it[nameLatin] = "Crab"
                it[nameRussian] = "Краб"
                it[kingdom] = jivotnieId
            }

            Organisms.insert {
                it[nameRussian] = "Мухомор"
                it[nameLatin] = "Muchomor"
                it[kingdom] = gribyId
            }
        }
    }
}