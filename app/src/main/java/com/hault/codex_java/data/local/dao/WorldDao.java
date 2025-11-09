package com.hault.codex_java.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.hault.codex_java.data.model.World;
import java.util.List;

@Dao
public interface WorldDao {
    @Insert
    void insert(World world);

    @Update
    void update(World world);

    @Delete
    void delete(World world);

    @Query("SELECT * FROM worlds ORDER BY name ASC")
    LiveData<List<World>> getAllWorlds();

    @Query("SELECT * FROM worlds WHERE id = :id")
    LiveData<World> getWorldById(int id);

    @Query("SELECT * FROM worlds ORDER BY name ASC")
    LiveData<List<World>> getAllWorldsSortedByNameASC();

    @Query("SELECT * FROM worlds ORDER BY name DESC")
    LiveData<List<World>> getAllWorldsSortedByNameDESC();

    @Query("SELECT * FROM worlds WHERE name LIKE :query ORDER BY name ASC")
    LiveData<List<World>> searchWorlds(String query);
}
