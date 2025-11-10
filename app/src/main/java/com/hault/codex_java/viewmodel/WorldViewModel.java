package com.hault.codex_java.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.hault.codex_java.data.local.specification.Specification;
import com.hault.codex_java.data.local.specification.WorldByNameSpecification;
import com.hault.codex_java.data.model.World;
import com.hault.codex_java.data.model.relations.WorldWithDetails;
import com.hault.codex_java.data.repository.WorldRepository;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class WorldViewModel extends ViewModel {
    private final WorldRepository repository;
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    public enum SortOrder { ASC, DESC }
    private final MutableLiveData<SortOrder> sortOrder = new MutableLiveData<>(SortOrder.ASC);

    private final LiveData<List<World>> worlds;

    @Inject
    public WorldViewModel(WorldRepository repository) {
        this.repository = repository;

        worlds = Transformations.switchMap(searchQuery, query ->
                Transformations.switchMap(sortOrder, order -> {
                    Specification spec = null;
                    if (query != null && !query.isEmpty()) {
                        spec = new WorldByNameSpecification(query);
                    }
                    String orderBy = "name " + (order == SortOrder.ASC ? "ASC" : "DESC");
                    return repository.searchWorlds(spec, orderBy);
                })
        );
    }

    public LiveData<List<World>> getWorlds() {
        return worlds;
    }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    public void setSortOrder(SortOrder order) {
        sortOrder.setValue(order);
    }

    public LiveData<World> getWorldById(int id) {
        return repository.getWorldById(id);
    }

    public LiveData<WorldWithDetails> getWorldWithDetails(int worldId) {
        return repository.worldDao.getWorldWithDetails(worldId);
    }

    public void insert(World world) {
        repository.insert(world);
    }

    public void update(World world) {
        repository.update(world);
    }

    public void delete(World world) {
        repository.delete(world);
    }
}