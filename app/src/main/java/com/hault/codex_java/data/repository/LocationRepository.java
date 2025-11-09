package com.hault.codex_java.data.repository;

import androidx.lifecycle.LiveData;
import com.hault.codex_java.data.local.dao.LocationDao;
import com.hault.codex_java.data.model.Location;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LocationRepository {
    private final LocationDao locationDao;
    private final ExecutorService executorService;

    public LocationRepository(LocationDao locationDao) {
        this.locationDao = locationDao;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Location>> getLocationsForWorld(int worldId) {
        return locationDao.getLocationsForWorld(worldId);
    }

    public void insert(Location location) {
        executorService.execute(() -> locationDao.insert(location));
    }

    public void update(Location location) {
        executorService.execute(() -> locationDao.update(location));
    }

    public void delete(Location location) {
        executorService.execute(() -> locationDao.delete(location));
    }

    public LiveData<Location> getLocationById(int id) {
        return locationDao.getLocationById(id);
    }

    public LiveData<List<Location>> searchLocationsForWorld(int worldId, String query) {
        return locationDao.searchLocationsForWorld(worldId, query);
    }
}