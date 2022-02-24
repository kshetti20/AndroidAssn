package com.example.androidapp.di

import android.content.Context
import androidx.room.Room
import com.example.androidapp.retrofit.QuotesApi
import com.example.androidapp.room_db.AppDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {
    
    @Singleton
    @Provides
    fun provieRoom(
        @ApplicationContext context: Context,
    ): AppDB {

       return Room.databaseBuilder(
           context,
            AppDB::class.java, "database-name"
        ).build()
    }


    val baseUrl = "https://quotable.io/"

    @Singleton
    @Provides
    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
    }

    @Singleton
    @Provides
    fun getRetrofitInstance(retrofit: Retrofit): QuotesApi {
        return retrofit.create(QuotesApi::class.java)
    }

}