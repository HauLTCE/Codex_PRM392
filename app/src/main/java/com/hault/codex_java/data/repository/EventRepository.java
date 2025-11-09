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
    private final EventDao eventDao;
    private final ExecutorService executorService;

    public EventRepository(EventDao eventDao) {
        this.eventDao = eventDao;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Event>> getEventsForWorld(int worldId) {
        return eventDao.getEventsForWorld(worldId);
    }

    public void insert(Event event) {
        executorService.execute(() -> eventDao.insert(event));
    }

    public void update(Event event) {
        executorService.execute(() -> eventDao.update(event));
    }

    public void delete(Event event) {
        executorService.execute(() -> eventDao.delete(event));
    }

    public LiveData<Event> getEvent(int id) {
        return eventDao.getEvent(id);
    }

    public LiveData<List<Event>> searchEventsForWorld(int worldId, String query) {
        return eventDao.searchEventsForWorld(worldId, query);
    }

}