package com.univalle.equipo5.webservice

class ApiUtils {
    companion object{
        fun getApiService():ApiService{
            return RetrofitClient.getRetrofit().create(ApiService::class.java)
        }
    }
}