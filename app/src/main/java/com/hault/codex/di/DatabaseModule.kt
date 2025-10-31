package com.hault.codex.di

import android.content.Context
import androidx.room.Room
import com.hault.codex.data.local.AppDatabase
import com.hault.codex.data.local.CharacterDao
import com.hault.codex.data.local.WorldDao
import com.hault.codex.data.repository.CharacterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "codex_database"
        ).build()
    }

    @Provides
    fun provideWorldDao(appDatabase: AppDatabase): WorldDao {
        return appDatabase.worldDao()
    }

    @Provides
    fun provideCharacterDao(appDatabase: AppDatabase): CharacterDao {
        return appDatabase.characterDao()
    }

    @Provides
    @Singleton
    fun provideCharacterRepository(characterDao: CharacterDao): CharacterRepository {
        return CharacterRepository(characterDao)
    }
}
