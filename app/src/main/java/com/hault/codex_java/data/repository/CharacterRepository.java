package com.hault.codex_java.data.repository;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;
import com.hault.codex_java.data.local.dao.CharacterDao;
import com.hault.codex_java.data.local.specification.Specification;
import com.hault.codex_java.data.model.Character;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CharacterRepository {
    public final CharacterDao characterDao;
    private final ExecutorService executorService;

    public CharacterRepository(CharacterDao characterDao) {
        this.characterDao = characterDao;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Character>> getCharactersForWorld(int worldId) {
        return characterDao.getCharactersForWorld(worldId);
    }

    public void insert(Character character) {
        character.lastModifiedAt = System.currentTimeMillis();
        executorService.execute(() -> characterDao.insert(character));
    }

    public void update(Character character) {
        character.lastModifiedAt = System.currentTimeMillis();
        executorService.execute(() -> characterDao.update(character));
    }

    public void delete(Character character) {
        executorService.execute(() -> characterDao.delete(character));
    }

    public LiveData<Character> getCharacter(int id) {
        return characterDao.getCharacter(id);
    }

    public LiveData<List<Character>> searchCharacters(Specification specification) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM characters");
        Object[] args = new Object[0];

        if (specification != null && specification.getSelectionClause() != null && !specification.getSelectionClause().isEmpty()) {
            sqlBuilder.append(" WHERE ").append(specification.getSelectionClause());
            args = specification.getSelectionArgs();
        }

        SupportSQLiteQuery query = new SimpleSQLiteQuery(sqlBuilder.toString(), args);
        return characterDao.search(query);
    }
}