package com.univalle.equipo5.webservice

import com.univalle.equipo5.data.dto.PokemonDTO
import com.univalle.equipo5.utils.Constants.END_POINT
import retrofit2.http.GET

interface ApiService {
    @GET(END_POINT)
    suspend fun getPokemon(): PokemonDTO
}