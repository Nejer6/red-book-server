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
            SchemaUtils.create(Kingdoms)
            SchemaUtils.create(Organisms)

            Kingdoms.insert {
                it[nameRussian] = "Грибы"
                it[nameLatin] = "Griby"
            }

            Kingdoms.insert {
                it[nameRussian] = "Животные"
                it[nameLatin] = "Jivotnie"
            }
        }
    }
}