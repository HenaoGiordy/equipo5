package com.univalle.equipo5.di

import android.content.Context
import com.univalle.equipo5.data.database.AppDatabase
import com.univalle.equipo5.data.dao.ChallengeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Este módulo vivirá durante todo el ciclo de la aplicación
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context) // Método para obtener la instancia de la base de datos
    }

    @Provides
    fun provideChallengeDao(database: AppDatabase): ChallengeDao {
        return database.ChallengeDao()
    }
}