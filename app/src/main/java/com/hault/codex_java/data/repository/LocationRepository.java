package com.hault.codex_java.data.repository;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.hault.codex_java.data.local.dao.LocationDao;
import com.hault.codex_java.data.local.specification.Specification;
import com.hault.codex_java.data.model.Location;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LocationRepository {
    public final LocationDao locationDao;
    private final ExecutorService executorService;

    public LocationRepository(LocationDao locationDao) {
        this.locationDao = locationDao;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Location>> getLocationsForWorld(int worldId) {
        return locationDao.getLocationsForWorld(worldId);
    }

    public void insert(Location location) {
        location.lastModifiedAt = System.currentTimeMillis();
        executorService.execute(() -> locationDao.insert(location));
    }

    public void update(Location location) {
        location.lastModifiedAt = System.currentTimeMillis();
        executorService.execute(() -> locationDao.update(location));
    }

    public void delete(Location location) {
        executorService.execute(() -> locationDao.delete(location));
    }

    public LiveData<Location> getLocationById(int id) {
        return locationDao.getLocationById(id);
    }

    public LiveData<List<Location>> searchLocations(Specification specification) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM locations");
        Object[] args = new Object[0];

        if (specification != null && specification.getSelectionClause() != null && !specification.getSelectionClause().isEmpty()) {
            sqlBuilder.append(" WHERE ").append(specification.getSelectionClause());
            args = specification.getSelectionArgs();
        }
        sqlBuilder.append(" ORDER BY name ASC");

        SupportSQLiteQuery query = new SimpleSQLiteQuery(sqlBuilder.toString(), args);
        return locationDao.search(query);
    }
}