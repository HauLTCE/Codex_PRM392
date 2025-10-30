package com.username.codex.data.repository

import com.username.codex.data.local.WorldDao
import com.username.codex.data.model.World
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorldRepository @Inject constructor(
    private val worldDao: WorldDao
) {
    val allWorlds: Flow<List<World>> = worldDao.getAllWorlds()

    suspend fun insert(world: World) {
        worldDao.insert(world)
    }
}
