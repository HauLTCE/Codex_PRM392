package com.hault.codex_java.data.repository;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.hault.codex_java.data.local.dao.ChapterDao;
import com.hault.codex_java.data.local.specification.Specification;
import com.hault.codex_java.data.model.Chapter;
import com.hault.codex_java.data.model.relations.ChapterWithDetails;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChapterRepository {
    private final ChapterDao chapterDao;
    private final ExecutorService executorService;

    public ChapterRepository(ChapterDao chapterDao) {
        this.chapterDao = chapterDao;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Chapter>> getChaptersForArc(int arcId) {
        return chapterDao.getChaptersForArc(arcId);
    }

    private int calculateWordCount(String prose) {
        if (prose == null || prose.trim().isEmpty()) {
            return 0;
        }
        // This regex splits the string by one or more whitespace characters.
        return prose.trim().split("\\s+").length;
    }

    public void insert(Chapter chapter) {
        chapter.lastModifiedAt = System.currentTimeMillis();
        chapter.wordCount = calculateWordCount(chapter.prose);
        executorService.execute(() -> chapterDao.insert(chapter));
    }

    public void update(Chapter chapter) {
        chapter.lastModifiedAt = System.currentTimeMillis();
        chapter.wordCount = calculateWordCount(chapter.prose);
        executorService.execute(() -> chapterDao.update(chapter));
    }

    public void delete(Chapter chapter) {
        executorService.execute(() -> chapterDao.delete(chapter));
    }

    public LiveData<Chapter> getChapter(int id) {
        return chapterDao.getChapter(id);
    }

    public LiveData<ChapterWithDetails> getChapterWithDetails(int chapterId) {
        return chapterDao.getChapterWithDetails(chapterId);
    }

    public LiveData<List<Chapter>> searchChapters(Specification specification) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM chapters");
        Object[] args = new Object[0];

        if (specification != null && specification.getSelectionClause() != null && !specification.getSelectionClause().isEmpty()) {
            sqlBuilder.append(" WHERE ").append(specification.getSelectionClause());
            args = specification.getSelectionArgs();
        }
        sqlBuilder.append(" ORDER BY chapterNumber ASC");

        SupportSQLiteQuery query = new SimpleSQLiteQuery(sqlBuilder.toString(), args);
        return chapterDao.search(query);
    }
}