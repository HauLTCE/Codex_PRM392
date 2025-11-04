package com.hault.codex.data.repository

import com.hault.codex.data.local.EventDao
import com.hault.codex.data.model.Event
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EventRepository @Inject constructor(private val eventDao: EventDao) {

    fun getEventsForWorld(worldId: Int): Flow<List<Event>> = eventDao.getEventsForWorld(worldId)

    suspend fun insert(event: Event) {
        eventDao.insert(event)
    }

    suspend fun update(event: Event) {
        eventDao.update(event)
    }

    suspend fun delete(event: Event) {
        eventDao.delete(event)
    }
}
