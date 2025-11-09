package com.hault.codex_java.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import com.hault.codex_java.data.local.specification.AndSpecification;
import com.hault.codex_java.data.local.specification.CharacterByNameSpecification;
import com.hault.codex_java.data.local.specification.CharacterByWorldSpecification;
import com.hault.codex_java.data.local.specification.Specification;
import com.hault.codex_java.data.model.Character;
import com.hault.codex_java.data.model.relations.CharacterWithDetails;
import com.hault.codex_java.data.repository.CharacterRepository;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CharacterViewModel extends ViewModel {
    private final CharacterRepository repository;
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    private final MutableLiveData<Integer> worldIdFilter = new MutableLiveData<>();

    private final LiveData<List<Character>> characters;

    @Inject
    public CharacterViewModel(CharacterRepository repository) {
        this.repository = repository;

        characters = Transformations.switchMap(worldIdFilter, worldId ->
                Transformations.switchMap(searchQuery, query -> {
                    Specification spec = new CharacterByWorldSpecification(worldId);
                    if (query != null && !query.isEmpty()) {
                        spec = new AndSpecification(spec, new CharacterByNameSpecification(query));
                    }
                    return repository.searchCharacters(spec);
                })
        );
    }

    public LiveData<List<Character>> getCharacters() {
        return characters;
    }

    public void setWorldId(int worldId) {
        worldIdFilter.setValue(worldId);
    }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    public LiveData<Character> getCharacter(int id) {
        return repository.getCharacter(id);
    }

    public LiveData<CharacterWithDetails> getCharacterWithDetails(int characterId) {
        return repository.characterDao.getCharacterWithDetails(characterId);
    }

    public void insert(Character character) {
        repository.insert(character);
    }

    public void update(Character character) {
        repository.update(character);
    }

    public void delete(Character character) {
        repository.delete(character);
    }
}