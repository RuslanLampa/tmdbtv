package com.tmdb.di

import android.content.Context
import androidx.room.Room
import com.tmdb.data.local.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "tmdb.db"
    )
        .fallbackToDestructiveMigration() // Not production solution
        .build()

    @Provides
    fun provideFavoritesDao(
        database: AppDatabase,
    ) = database.favoritesDao()
}