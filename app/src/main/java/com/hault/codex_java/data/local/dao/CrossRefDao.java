package com.hault.codex_java.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.hault.codex_java.data.model.crossref.ChapterCharacterCrossRef;
import com.hault.codex_java.data.model.crossref.ChapterEventCrossRef;
import com.hault.codex_java.data.model.crossref.ChapterLocationCrossRef;
import com.hault.codex_java.data.model.crossref.CharacterCharacterCrossRef;
import com.hault.codex_java.data.model.crossref.EventCharacterCrossRef;
import com.hault.codex_java.data.model.crossref.EventLocationCrossRef;

import java.util.List;

@Dao
public interface CrossRefDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CharacterCharacterCrossRef crossRef);

    @Update
    void update(CharacterCharacterCrossRef crossRef);

    @Delete
    void delete(CharacterCharacterCrossRef crossRef);

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

    @Query("SELECT * FROM character_character_cross_ref WHERE characterOneId = :characterId")
    LiveData<List<CharacterCharacterCrossRef>> getRelationshipsForCharacter(int characterId);

}