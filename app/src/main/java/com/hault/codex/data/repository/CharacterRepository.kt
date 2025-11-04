package com.hault.codex.data.repository

import com.hault.codex.data.local.CharacterDao
import com.hault.codex.data.model.Character
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepository @Inject constructor(
    private val characterDao: CharacterDao
) {
    suspend fun insert(character: Character) {
        characterDao.insert(character)
    }

    fun getCharactersForWorld(worldId: Int): Flow<List<Character>> {
        return characterDao.getCharactersForWorld(worldId)
    }

    suspend fun getCharacter(id: Int): Character? {
        return characterDao.getCharacter(id)
    }

    suspend fun update(character: Character) {
        characterDao.update(character)
    }

    suspend fun delete(character: Character) {
        characterDao.delete(character)
    }
}
