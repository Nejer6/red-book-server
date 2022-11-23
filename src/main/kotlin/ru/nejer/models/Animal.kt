package ru.nejer.models


@kotlinx.serialization.Serializable
data class Animal(
    val id: Int,
    val name: String?,
    val nameRu: String,
    val kingdom: String,
    val rare: String,
    val region: String,
    val animalClass: String?
)
