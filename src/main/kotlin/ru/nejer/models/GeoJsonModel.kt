package ru.nejer.models

import kotlinx.serialization.Serializable

@Serializable
data class GeoJsonModel(
    val type: String,
    val name: String,
    val crs: Crs,
    val features: List<Feature>
)

@Serializable
data class Crs(
    val type: String,
    val properties: PropertiesCrs
)

@Serializable
data class PropertiesCrs(
    val name: String
)

@Serializable
data class Feature(
    val type: String = "Feature",
    val properties: Properties,
    val geometry: Geometry
)

@Serializable
data class Properties(
    val id: Int?,
    val nameRu: String,
    val name: String?,
    val rare: String,
    val adm_name: String,
    val animalClass: String? = null
)

@Serializable
data class Geometry(
    val type: String = "MultiPolygon",
    val coordinates: List<List<List<List<Double>>>>
)
