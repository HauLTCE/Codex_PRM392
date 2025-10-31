package com.hault.codex.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hault.codex.data.model.Character
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: Character)

    @Query("SELECT * FROM characters WHERE worldId = :worldId")
    fun getCharactersForWorld(worldId: Int): Flow<List<Character>>
}
