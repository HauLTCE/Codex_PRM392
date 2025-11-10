package com.hault.codex_java.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.hault.codex_java.data.model.crossref.ChapterCharacterCrossRef;
import com.hault.codex_java.data.model.crossref.ChapterEventCrossRef;
import com.hault.codex_java.data.model.crossref.ChapterLocationCrossRef;
import com.hault.codex_java.data.model.crossref.CharacterCharacterCrossRef;
import com.hault.codex_java.data.model.crossref.EventCharacterCrossRef;
import com.hault.codex_java.data.model.crossref.EventLocationCrossRef;
import com.hault.codex_java.data.repository.CrossRefRepository;

import java.util.List;

import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CrossRefViewModel extends ViewModel {
    private final CrossRefRepository repository;

    @Inject
    public CrossRefViewModel(CrossRefRepository repository) {
        this.repository = repository;
    }

    public void insert(CharacterCharacterCrossRef ref) {
        repository.insert(ref);
    }

    public void update(CharacterCharacterCrossRef ref) {
        repository.update(ref);
    }

    public void delete(CharacterCharacterCrossRef ref) {
        repository.delete(ref);
    }

    public void insert(EventCharacterCrossRef ref) {
        repository.insert(ref);
    }

    public void insert(EventLocationCrossRef ref) {
        repository.insert(ref);
    }

    public void insert(ChapterCharacterCrossRef ref) {
        repository.insert(ref);
    }

    public void insert(ChapterLocationCrossRef ref) {
        repository.insert(ref);
    }

    public void insert(ChapterEventCrossRef ref) {
        repository.insert(ref);
    }

    public LiveData<List<CharacterCharacterCrossRef>> getRelationshipsForCharacter(int characterId) {
        return repository.crossRefDao.getRelationshipsForCharacter(characterId);
    }
}