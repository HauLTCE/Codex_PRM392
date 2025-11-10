package com.hault.codex_java.data.repository;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.hault.codex_java.data.local.dao.ArcDao;
import com.hault.codex_java.data.local.specification.Specification;
import com.hault.codex_java.data.model.Arc;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ArcRepository {
    public final ArcDao arcDao;
    private final ExecutorService executorService;

    public ArcRepository(ArcDao arcDao) {
        this.arcDao = arcDao;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Arc>> getArcsForWorld(int worldId) {
        return arcDao.getArcsForWorld(worldId);
    }

    public void insert(Arc arc) {
        arc.lastModifiedAt = System.currentTimeMillis();
        executorService.execute(() -> arcDao.insert(arc));
    }

    public void update(Arc arc) {
        arc.lastModifiedAt = System.currentTimeMillis();
        executorService.execute(() -> arcDao.update(arc));
    }

    public void update(List<Arc> arcs) {
        long currentTime = System.currentTimeMillis();
        for (Arc arc : arcs) {
            arc.lastModifiedAt = currentTime;
        }
        executorService.execute(() -> arcDao.update(arcs));
    }

    public void delete(Arc arc) {
        executorService.execute(() -> arcDao.delete(arc));
    }

    public LiveData<Arc> getArc(int id) {
        return arcDao.getArc(id);
    }

    public LiveData<List<Arc>> searchArcs(Specification specification) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM arcs");
        Object[] args = new Object[0];

        if (specification != null && specification.getSelectionClause() != null && !specification.getSelectionClause().isEmpty()) {
            sqlBuilder.append(" WHERE ").append(specification.getSelectionClause());
            args = specification.getSelectionArgs();
        }
        sqlBuilder.append(" ORDER BY isPinned DESC, displayOrder ASC");

        SupportSQLiteQuery query = new SimpleSQLiteQuery(sqlBuilder.toString(), args);
        return arcDao.search(query);
    }
}