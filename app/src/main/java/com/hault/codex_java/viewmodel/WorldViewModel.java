package com.hault.codex_java.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import com.hault.codex_java.data.model.World;
import com.hault.codex_java.data.repository.WorldRepository;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class WorldViewModel extends ViewModel {
    private final WorldRepository repository;
    private final MediatorLiveData<List<World>> worldList = new MediatorLiveData<>();
    private LiveData<List<World>> currentSource;

    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");

    public enum SortOrder { ASC, DESC }
    private final MutableLiveData<SortOrder> sortOrder = new MutableLiveData<>(SortOrder.ASC);

    @Inject
    public WorldViewModel(WorldRepository repository) {
        this.repository = repository;
        observeData();
    }

    private void observeData() {
        worldList.removeSource(currentSource); // Remove previous source

        currentSource = Transformations.switchMap(searchQuery, query -> {
            if (query == null || query.isEmpty()) {
                return Transformations.switchMap(sortOrder, order -> {
                    if (order == SortOrder.ASC) {
                        return repository.getAllWorldsSortedByNameASC();
                    } else {
                        return repository.getAllWorldsSortedByNameDESC();
                    }
                });
            } else {
                return repository.searchWorlds("%" + query + "%");
            }
        });

        worldList.addSource(currentSource, worldList::setValue);
    }

    public LiveData<List<World>> getWorlds() {
        return worldList;
    }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    public void setSortOrder(SortOrder order) {
        sortOrder.setValue(order);
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
    public LiveData<World> getWorldById(int id) {
        return repository.getWorldById(id);
    }
}