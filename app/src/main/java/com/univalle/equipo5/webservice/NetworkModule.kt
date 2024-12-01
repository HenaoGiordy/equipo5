package com.univalle.equipo5.webservice

import com.google.firebase.firestore.FirebaseFirestore
import com.univalle.equipo5.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.univalle.equipo5.repository.ChallengeRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    // Proveemos FirebaseFirestore
    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()  // Devuelve la instancia de Firestore
    }

    @Provides
    fun provideChallengeRepository(firestore: FirebaseFirestore): ChallengeRepository {
        return ChallengeRepository(firestore)  // Inyectamos el firestore en el repositorio
    }
}
