package com.challenge.movieappchallenge.data.di

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.challenge.movieappchallenge.data.local.MoviesDao
import com.challenge.movieappchallenge.data.models.MovieLocal
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDBModule {

    @Provides
    @Singleton
    fun provideRoomDB(
        applicationContext: Application
    ): MoviesDao {
        return Room.databaseBuilder(
            (applicationContext as Context),
            AppDatabase::class.java, "movie-db"
        ).build().movieDao()
    }
}

@Database(entities = [MovieLocal::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MoviesDao
}
