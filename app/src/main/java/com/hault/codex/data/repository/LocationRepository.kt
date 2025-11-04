package com.hault.codex.data.repository

import com.hault.codex.data.local.LocationDao
import com.hault.codex.data.model.Location
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepository @Inject constructor(
    private val locationDao: LocationDao
) {
    suspend fun insert(location: Location) {
        locationDao.insert(location)
    }

    suspend fun update(location: Location) {
        locationDao.update(location)
    }

    suspend fun delete(location: Location) {
        locationDao.delete(location)
    }

    fun getLocationsForWorld(worldId: Int): Flow<List<Location>> {
        return locationDao.getLocationsForWorld(worldId)
    }

    suspend fun getLocationById(id: Int): Location? {
        return locationDao.getLocationById(id)
    }
}