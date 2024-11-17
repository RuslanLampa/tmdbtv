package com.tmdb.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tmdb.data.local.room.dao.FavoriteMoviesDao
import com.tmdb.data.local.room.entity.FavoriteMovieEntity

@Database(entities = [FavoriteMovieEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoriteMoviesDao
}