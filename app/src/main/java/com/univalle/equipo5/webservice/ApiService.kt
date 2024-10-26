package com.univalle.equipo5.webservice

import com.univalle.equipo5.data.dto.PokemonResponse
import com.univalle.equipo5.utils.Constants.END_POINT
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET(END_POINT)
    suspend fun getPokemons(): Response<PokemonResponse>
}