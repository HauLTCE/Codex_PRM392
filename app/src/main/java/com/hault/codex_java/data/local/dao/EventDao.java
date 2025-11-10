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

import com.hault.codex_java.data.model.Event;
import com.hault.codex_java.data.model.relations.EventWithDetails;

import java.util.List;

@Dao
public interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Event event);

    @Update
    void update(Event event);

    @Delete
    void delete(Event event);

    @Query("SELECT * FROM events WHERE id = :id")
    LiveData<Event> getEvent(int id);

    @Query("SELECT * FROM events WHERE worldId = :worldId ORDER BY date")
    LiveData<List<Event>> getEventsForWorld(int worldId);

    @RawQuery(observedEntities = Event.class)
    LiveData<List<Event>> search(SupportSQLiteQuery query);

    @Transaction
    @Query("SELECT * FROM events WHERE id = :eventId")
    LiveData<EventWithDetails> getEventWithDetails(int eventId);
}