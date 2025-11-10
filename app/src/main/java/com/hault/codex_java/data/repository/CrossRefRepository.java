package com.hault.codex_java.data.repository;

import com.hault.codex_java.data.local.dao.CrossRefDao;
import com.hault.codex_java.data.model.crossref.ChapterCharacterCrossRef;
import com.hault.codex_java.data.model.crossref.ChapterEventCrossRef;
import com.hault.codex_java.data.model.crossref.ChapterLocationCrossRef;
import com.hault.codex_java.data.model.crossref.CharacterCharacterCrossRef;
import com.hault.codex_java.data.model.crossref.EventCharacterCrossRef;
import com.hault.codex_java.data.model.crossref.EventLocationCrossRef;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CrossRefRepository {
    public final CrossRefDao crossRefDao;
    private final ExecutorService executorService;

    public CrossRefRepository(CrossRefDao crossRefDao) {
        this.crossRefDao = crossRefDao;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(CharacterCharacterCrossRef ref) {
        executorService.execute(() -> crossRefDao.insert(ref));
    }

    public void update(CharacterCharacterCrossRef ref) {
        executorService.execute(() -> crossRefDao.update(ref));
    }

    public void delete(CharacterCharacterCrossRef ref) {
        executorService.execute(() -> crossRefDao.delete(ref));
    }

    public void insert(EventCharacterCrossRef ref) {
        executorService.execute(() -> crossRefDao.insert(ref));
    }

    public void insert(EventLocationCrossRef ref) {
        executorService.execute(() -> crossRefDao.insert(ref));
    }

    public void insert(ChapterCharacterCrossRef ref) {
        executorService.execute(() -> crossRefDao.insert(ref));
    }

    public void insert(ChapterLocationCrossRef ref) {
        executorService.execute(() -> crossRefDao.insert(ref));
    }

    public void insert(ChapterEventCrossRef ref) {
        executorService.execute(() -> crossRefDao.insert(ref));
    }
}