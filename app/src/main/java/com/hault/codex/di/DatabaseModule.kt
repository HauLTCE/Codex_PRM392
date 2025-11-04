package com.hault.codex.di

import android.content.Context
import androidx.room.Room
import com.hault.codex.data.local.AppDatabase
import com.hault.codex.data.local.CharacterDao
import com.hault.codex.data.local.WorldDao
import com.hault.codex.data.local.LocationDao
import com.hault.codex.data.repository.CharacterRepository
import com.hault.codex.data.repository.LocationRepository
import com.hault.codex.data.local.EventDao
import com.hault.codex.data.repository.EventRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS events (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL, description TEXT, date TEXT NOT NULL, worldId INTEGER NOT NULL, FOREIGN KEY(worldId) REFERENCES worlds(id) ON UPDATE NO ACTION ON DELETE CASCADE)")
            database.execSQL("CREATE INDEX IF NOT EXISTS index_events_worldId ON events(worldId)")
        }
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "codex_database"
        ).addMigrations(MIGRATION_2_3).build()
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
    fun provideLocationDao(appDatabase: AppDatabase): LocationDao {
        return appDatabase.locationDao()
    }

    @Provides
    fun provideEventDao(appDatabase: AppDatabase): EventDao {
        return appDatabase.eventDao()
    }

    @Provides
    @Singleton
    fun provideCharacterRepository(characterDao: CharacterDao): CharacterRepository {
        return CharacterRepository(characterDao)
    }

    @Provides
    @Singleton
    fun provideLocationRepository(locationDao: LocationDao): LocationRepository {
        return LocationRepository(locationDao)
    }

    @Provides
    @Singleton
    fun provideEventRepository(eventDao: EventDao): EventRepository {
        return EventRepository(eventDao)
    }
}
