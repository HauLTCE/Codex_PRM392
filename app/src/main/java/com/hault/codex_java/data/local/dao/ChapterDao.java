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

import com.hault.codex_java.data.model.Chapter;
import com.hault.codex_java.data.model.relations.ChapterWithDetails;

import java.util.List;

@Dao
public interface ChapterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Chapter chapter);

    @Update
    void update(Chapter chapter);

    @Delete
    void delete(Chapter chapter);

    @Query("SELECT * FROM chapters WHERE id = :id")
    LiveData<Chapter> getChapter(int id);

    @Query("SELECT * FROM chapters WHERE arcId = :arcId ORDER BY chapterNumber ASC")
    LiveData<List<Chapter>> getChaptersForArc(int arcId);

    @RawQuery(observedEntities = Chapter.class)
    LiveData<List<Chapter>> search(SupportSQLiteQuery query);

    @Transaction
    @Query("SELECT * FROM chapters WHERE id = :chapterId")
    LiveData<ChapterWithDetails> getChapterWithDetails(int chapterId);
}