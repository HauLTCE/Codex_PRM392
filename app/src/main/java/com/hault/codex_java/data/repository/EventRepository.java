package com.hault.codex_java.data.repository;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;
import com.hault.codex_java.data.local.dao.EventDao;
import com.hault.codex_java.data.local.specification.Specification;
import com.hault.codex_java.data.model.Event;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventRepository {
    public final EventDao eventDao;
    private final ExecutorService executorService;

    public EventRepository(EventDao eventDao) {
        this.eventDao = eventDao;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Event>> getEventsForWorld(int worldId) {
        return eventDao.getEventsForWorld(worldId);
    }

    public void insert(Event event) {
        event.lastModifiedAt = System.currentTimeMillis();
        executorService.execute(() -> eventDao.insert(event));
    }

    public void update(Event event) {
        event.lastModifiedAt = System.currentTimeMillis();
        executorService.execute(() -> eventDao.update(event));
    }

    public void delete(Event event) {
        executorService.execute(() -> eventDao.delete(event));
    }

    public LiveData<Event> getEvent(int id) {
        return eventDao.getEvent(id);
    }

    public LiveData<List<Event>> searchEvents(Specification specification) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM events");
        Object[] args = new Object[0];

        if (specification != null && specification.getSelectionClause() != null && !specification.getSelectionClause().isEmpty()) {
            sqlBuilder.append(" WHERE ").append(specification.getSelectionClause());
            args = specification.getSelectionArgs();
        }
        sqlBuilder.append(" ORDER BY isPinned DESC, date ASC");

        SupportSQLiteQuery query = new SimpleSQLiteQuery(sqlBuilder.toString(), args);
        return eventDao.search(query);
    }
}