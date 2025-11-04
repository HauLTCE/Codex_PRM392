package com.hault.codex.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hault.codex.data.model.Character
import com.hault.codex.data.model.Event
import com.hault.codex.data.model.World
import com.hault.codex.data.model.Location

@Database(entities = [World::class, Character::class, Location::class, Event::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun worldDao(): WorldDao
    abstract fun characterDao(): CharacterDao
    abstract fun locationDao(): LocationDao
    abstract fun eventDao(): EventDao
}
