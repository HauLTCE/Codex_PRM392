package com.hault.codex.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hault.codex.data.model.Character
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Insert
    suspend fun insert(character: Character)

    @Update
    suspend fun update(character: Character)

    @Delete
    suspend fun delete(character: Character)

    @Query("SELECT * FROM characters WHERE worldId = :worldId ORDER BY name ASC")
    fun getCharactersForWorld(worldId: Int): Flow<List<Character>>
}
