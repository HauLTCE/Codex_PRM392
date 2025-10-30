package com.username.codex

import android.content.Context
import androidx.room.Room
import com.username.codex.data.local.AppDatabase
import com.username.codex.data.local.CharacterDao
import com.username.codex.data.local.WorldDao
import com.username.codex.di.DatabaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object TestDatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @Provides
    fun provideWorldDao(appDatabase: AppDatabase): WorldDao {
        return appDatabase.worldDao()
    }

    @Provides
    fun provideCharacterDao(appDatabase: AppDatabase): CharacterDao {
        return appDatabase.characterDao()
    }
}
