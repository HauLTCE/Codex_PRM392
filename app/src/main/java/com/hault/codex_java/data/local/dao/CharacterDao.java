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

import com.hault.codex_java.data.model.Character;
import com.hault.codex_java.data.model.relations.CharacterWithDetails;

import java.util.List;

@Dao
public interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Character character);

    @Update
    void update(Character character);

    @Delete
    void delete(Character character);

    @Query("SELECT * FROM characters WHERE id = :id")
    LiveData<Character> getCharacter(int id);

    @Query("SELECT * FROM characters WHERE worldId = :worldId")
    LiveData<List<Character>> getCharactersForWorld(int worldId);

    @RawQuery(observedEntities = Character.class)
    LiveData<List<Character>> search(SupportSQLiteQuery query);

    @Transaction
    @Query("SELECT * FROM characters WHERE id = :characterId")
    LiveData<CharacterWithDetails> getCharacterWithDetails(int characterId);
}