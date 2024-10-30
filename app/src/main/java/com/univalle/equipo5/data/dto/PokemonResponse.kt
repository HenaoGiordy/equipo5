package com.univalle.equipo5.data.dto

import com.google.gson.annotations.SerializedName

data class PokemonResponse(
    @SerializedName("pokemon")
    val pokemonList: List<PokemonDTO>?
)
