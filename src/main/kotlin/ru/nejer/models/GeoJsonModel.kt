package ru.nejer.models

@kotlinx.serialization.Serializable
data class GeoJsonModel(
    val type: String,
    val features: List<Feature>,
)

@kotlinx.serialization.Serializable
data class Feature(
    val type: String,
    val geometry: MultiPolygon,
    val properties: Properties
)

@kotlinx.serialization.Serializable
data class MultiPolygon(
    val type: String,
    val coordinates: List<List<List<List<Double>>>>
)

@kotlinx.serialization.Serializable
data class Properties(
    val osm_id: Int,
    val boundary: String,
    val admin_level: Int,
    val parents: String,
    val name: String,
    val local_name: String,
    val name_en: String?
)
