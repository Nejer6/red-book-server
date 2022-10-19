package ru.nejer.json

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ru.nejer.models.GeoJsonModel
import java.io.File

object JsonParser {
    val regions: GeoJsonModel by lazy {
        Json.decodeFromString(File("json/regions.geojson").readText(Charsets.UTF_8))
    }

    val animals: GeoJsonModel by lazy {
        Json.decodeFromString(File("json/rastenia_poly.geojson").readText(Charsets.UTF_8))
    }
}