package com.username.codex.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.username.codex.data.model.Character
import com.username.codex.data.model.World

@Database(entities = [World::class, Character::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun worldDao(): WorldDao
    abstract fun characterDao(): CharacterDao
}
