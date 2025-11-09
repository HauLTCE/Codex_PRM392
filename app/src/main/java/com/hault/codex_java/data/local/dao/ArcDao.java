package com.hault.codex_java.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.hault.codex_java.data.model.Arc;
import com.hault.codex_java.data.model.relations.ArcWithDetails;

import java.util.List;

@Dao
public interface ArcDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Arc arc);

    @Update
    void update(Arc arc);

    @Delete
    void delete(Arc arc);

    @Query("SELECT * FROM arcs WHERE id = :id")
    LiveData<Arc> getArc(int id);

    @Query("SELECT * FROM arcs WHERE worldId = :worldId ORDER BY displayOrder ASC")
    LiveData<List<Arc>> getArcsForWorld(int worldId);

    @RawQuery(observedEntities = Arc.class)
    LiveData<List<Arc>> search(SupportSQLiteQuery query);

    @Transaction
    @Query("SELECT * FROM arcs WHERE id = :arcId")
    LiveData<ArcWithDetails> getArcWithDetails(int arcId);
}