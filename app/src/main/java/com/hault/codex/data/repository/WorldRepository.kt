package com.hault.codex.data.repository

import com.hault.codex.data.local.WorldDao
import com.hault.codex.data.model.World
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles all data operations for Worlds.
 * It provides a clean API to the rest of the app for accessing world data.
 * The @Singleton annotation ensures there is only one instance of this repository in the app.
 */
//@Singleton
class WorldRepository @Inject constructor(private val worldDao: WorldDao) {

    // Exposes a Flow of all worlds from the DAO. The UI can observe this for live updates.
    val allWorlds: Flow<List<World>> = worldDao.getAllWorlds()

    // A suspend function to insert a new world. This can only be called from a coroutine.
    suspend fun insert(world: World) {
        worldDao.insert(world)
    }

    // We can add update and delete functions here later when we need them.
    // suspend fun update(world: World) {
    //     worldDao.update(world)
    // }
    //
    // suspend fun delete(world: World) {
    //     worldDao.delete(world)
    // }

    suspend fun getWorldById(id: Int): World? {
        return worldDao.getWorldById(id)
    }

    suspend fun update(world: World) {
        worldDao.update(world)
    }

    suspend fun delete(world: World) {
        worldDao.delete(world)
    }
}