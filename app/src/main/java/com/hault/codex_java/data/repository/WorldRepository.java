package com.hault.codex_java.data.repository;

import androidx.lifecycle.LiveData;
import com.hault.codex_java.data.local.dao.WorldDao;
import com.hault.codex_java.data.model.World;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorldRepository {
    private final WorldDao worldDao;
    private final LiveData<List<World>> allWorlds;
    private final ExecutorService executorService;

    public WorldRepository(WorldDao worldDao) {
        this.worldDao = worldDao;
        this.allWorlds = worldDao.getAllWorlds();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<World>> getAllWorlds() {
        return allWorlds;
    }

    public void insert(World world) {
        executorService.execute(() -> worldDao.insert(world));
    }

    public void update(World world) {
        executorService.execute(() -> worldDao.update(world));
    }

    public void delete(World world) {
        executorService.execute(() -> worldDao.delete(world));
    }

    public LiveData<World> getWorldById(int id) {
        return worldDao.getWorldById(id);
    }

    public LiveData<List<World>> getAllWorldsSortedByNameASC() {
        return worldDao.getAllWorldsSortedByNameASC();
    }

    public LiveData<List<World>> getAllWorldsSortedByNameDESC() {
        return worldDao.getAllWorldsSortedByNameDESC();
    }

    public LiveData<List<World>> searchWorlds(String query) {
        return worldDao.searchWorlds(query);
    }
}