package com.hault.codex.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hault.codex.data.model.Location
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: Location)

    @Update
    suspend fun update(location: Location)

    @Delete
    suspend fun delete(location: Location)

    @Query("SELECT * FROM locations WHERE worldId = :worldId ORDER BY name ASC")
    fun getLocationsForWorld(worldId: Int): Flow<List<Location>>

    @Query("SELECT * FROM locations WHERE id = :id")
    suspend fun getLocationById(id: Int): Location?
}