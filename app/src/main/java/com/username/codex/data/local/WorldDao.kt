package com.username.codex.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.username.codex.data.model.World
import kotlinx.coroutines.flow.Flow

@Dao
interface WorldDao {

    @Insert
    suspend fun insert(world: World)

    @Update
    suspend fun update(world: World)

    @Delete
    suspend fun delete(world: World)

    @Query("SELECT * FROM worlds ORDER BY name ASC")
    fun getAllWorlds(): Flow<List<World>>
}
