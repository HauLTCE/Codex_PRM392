package com.hault.codex_java.data.local.dao;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;
import com.hault.codex_java.data.model.World;
import com.hault.codex_java.data.model.relations.WorldWithDetails;
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

    @RawQuery(observedEntities = World.class)
    LiveData<List<World>> search(SupportSQLiteQuery query);

    @Transaction
    @Query("SELECT * FROM worlds WHERE id = :worldId")
    LiveData<WorldWithDetails> getWorldWithDetails(int worldId);
}