package com.univalle.equipo5.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.univalle.equipo5.data.dto.PokemonDTO
import com.univalle.equipo5.data.dto.PokemonResponse
import com.univalle.equipo5.webservice.ApiUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonViewModel:ViewModel() {
    private val apiService = ApiUtils.getApiService()

    suspend fun fetchPokemons(): String {
        return withContext(Dispatchers.IO) {
            // Supongamos que tienes un Retrofit service configurado
            try {
                val response = apiService.getPokemons() // Llamada suspendida
                if (response.isSuccessful) {
                    // Si la respuesta es exitosa, devuelve la URL de la primera imagen
                    response.body()?.pokemonList?.randomOrNull()?.imagenUrl ?: "No image found"
                } else {
                    Log.e("Error", "Error al obtener datos del Pokémon")
                    "Error en la respuesta"
                }
            } catch (e: Exception) {
                Log.e("Error", "Error de red: ${e.message}")
                "Network error"
            }
        }
    }
}