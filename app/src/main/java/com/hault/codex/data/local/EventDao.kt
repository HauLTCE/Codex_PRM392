package com.hault.codex.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hault.codex.data.model.Event
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: Event)

    @Update
    suspend fun update(event: Event)

    @Delete
    suspend fun delete(event: Event)

    @Query("SELECT * FROM events WHERE worldId = :worldId ORDER BY date")
    fun getEventsForWorld(worldId: Int): Flow<List<Event>>

    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getEvent(id: Int): Event?
}
