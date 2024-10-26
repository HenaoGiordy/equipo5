package com.univalle.equipo5.data.dto

import com.google.gson.annotations.SerializedName

data class PokemonDTO(
    @SerializedName("img")
    val imagenUrl: String
)
