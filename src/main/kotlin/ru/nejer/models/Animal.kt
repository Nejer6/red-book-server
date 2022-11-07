package ru.nejer.models


@kotlinx.serialization.Serializable
data class Animal(
    val id: Int,
    val name: String?,
    val nameRu: String,
    val kingdom: String,
    val rare: String,
    val adm_name: String
)
