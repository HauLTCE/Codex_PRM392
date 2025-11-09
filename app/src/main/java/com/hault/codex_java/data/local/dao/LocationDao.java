package com.hault.codex_java.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.hault.codex_java.data.model.Location;
import java.util.List;

@Dao
public interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Location location);

    @Update
    void update(Location location);

    @Delete
    void delete(Location location);

    @Query("SELECT * FROM locations WHERE worldId = :worldId ORDER BY name ASC")
    LiveData<List<Location>> getLocationsForWorld(int worldId);

    @Query("SELECT * FROM locations WHERE id = :id")
    LiveData<Location> getLocationById(int id);

    @Query("SELECT * FROM locations WHERE worldId = :worldId AND name LIKE :query ORDER BY name ASC")
    LiveData<List<Location>> searchLocationsForWorld(int worldId, String query);
}
