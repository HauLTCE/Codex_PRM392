package com.hault.codex_java.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.hault.codex_java.data.model.crossref.ChapterCharacterCrossRef;
import com.hault.codex_java.data.model.crossref.ChapterEventCrossRef;
import com.hault.codex_java.data.model.crossref.ChapterLocationCrossRef;
import com.hault.codex_java.data.model.crossref.CharacterCharacterCrossRef;
import com.hault.codex_java.data.model.crossref.EventCharacterCrossRef;
import com.hault.codex_java.data.model.crossref.EventLocationCrossRef;

@Dao
public interface CrossRefDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CharacterCharacterCrossRef crossRef);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(EventCharacterCrossRef crossRef);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(EventLocationCrossRef crossRef);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ChapterCharacterCrossRef crossRef);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ChapterLocationCrossRef crossRef);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ChapterEventCrossRef crossRef);

}