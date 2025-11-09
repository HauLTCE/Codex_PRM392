package com.hault.codex_java.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.hault.codex_java.data.local.specification.AndSpecification;
import com.hault.codex_java.data.local.specification.ArcByTitleSpecification;
import com.hault.codex_java.data.local.specification.ArcByWorldSpecification;
import com.hault.codex_java.data.local.specification.Specification;
import com.hault.codex_java.data.model.Arc;
import com.hault.codex_java.data.model.relations.ArcWithDetails;
import com.hault.codex_java.data.repository.ArcRepository;

import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ArcViewModel extends ViewModel {
    private final ArcRepository repository;
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    private final MutableLiveData<Integer> worldIdFilter = new MutableLiveData<>();

    private final LiveData<List<Arc>> arcs;

    @Inject
    public ArcViewModel(ArcRepository repository) {
        this.repository = repository;

        arcs = Transformations.switchMap(worldIdFilter, worldId ->
                Transformations.switchMap(searchQuery, query -> {
                    Specification spec = new ArcByWorldSpecification(worldId);
                    if (query != null && !query.isEmpty()) {
                        spec = new AndSpecification(spec, new ArcByTitleSpecification(query));
                    }
                    return repository.searchArcs(spec);
                })
        );
    }

    public LiveData<List<Arc>> getArcs() {
        return arcs;
    }

    public void setWorldId(int worldId) {
        worldIdFilter.setValue(worldId);
    }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    public LiveData<Arc> getArcById(int id) {
        return repository.getArc(id);
    }

    public LiveData<ArcWithDetails> getArcWithDetails(int arcId) {
        return repository.arcDao.getArcWithDetails(arcId);
    }

    public void insert(Arc arc) {
        repository.insert(arc);
    }

    public void update(Arc arc) {
        repository.update(arc);
    }

    public void delete(Arc arc) {
        repository.delete(arc);
    }
}