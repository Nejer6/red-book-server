package ru.nejer.models


@kotlinx.serialization.Serializable
data class Animal(
    val id: Int,
    val name: String?,
    val nameRu: String,
    val rare: String
)
