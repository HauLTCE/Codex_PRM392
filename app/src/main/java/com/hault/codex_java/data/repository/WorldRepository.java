package com.hault.codex_java.data.repository;
import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;
import com.hault.codex_java.data.local.dao.WorldDao;
import com.hault.codex_java.data.local.specification.Specification;
import com.hault.codex_java.data.model.World;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class WorldRepository {
    public final WorldDao worldDao;
    private final ExecutorService executorService;

    public WorldRepository(WorldDao worldDao) {
        this.worldDao = worldDao;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<World>> getAllWorlds() {
        return worldDao.getAllWorlds();
    }

    public void insert(World world) {
        world.lastModifiedAt = System.currentTimeMillis();
        executorService.execute(() -> worldDao.insert(world));
    }

    public void update(World world) {
        world.lastModifiedAt = System.currentTimeMillis();
        executorService.execute(() -> worldDao.update(world));
    }

    public void delete(World world) {
        executorService.execute(() -> worldDao.delete(world));
    }

    public LiveData<World> getWorldById(int id) {
        return worldDao.getWorldById(id);
    }

    public LiveData<List<World>> searchWorlds(Specification specification, String orderBy) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM worlds");
        Object[] args = new Object[0];

        if (specification != null && specification.getSelectionClause() != null && !specification.getSelectionClause().isEmpty()) {
            sqlBuilder.append(" WHERE ").append(specification.getSelectionClause());
            args = specification.getSelectionArgs();
        }

        if (orderBy != null && !orderBy.isEmpty()) {
            sqlBuilder.append(" ORDER BY ").append(orderBy);
        }

        SupportSQLiteQuery query = new SimpleSQLiteQuery(sqlBuilder.toString(), args);
        return worldDao.search(query);
    }
}