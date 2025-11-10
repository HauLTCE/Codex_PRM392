package com.hault.codex_java.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.hault.codex_java.data.local.specification.AndSpecification;
import com.hault.codex_java.data.local.specification.LocationByNameSpecification;
import com.hault.codex_java.data.local.specification.LocationByWorldSpecification;
import com.hault.codex_java.data.local.specification.Specification;
import com.hault.codex_java.data.model.Location;
import com.hault.codex_java.data.model.relations.LocationWithDetails;
import com.hault.codex_java.data.repository.LocationRepository;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LocationViewModel extends ViewModel {
    private final LocationRepository repository;

    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    private final MutableLiveData<Integer> worldIdFilter = new MutableLiveData<>();

    private final LiveData<List<Location>> locations;

    @Inject
    public LocationViewModel(LocationRepository repository) {
        this.repository = repository;

        locations = Transformations.switchMap(worldIdFilter, worldId ->
                Transformations.switchMap(searchQuery, query -> {
                    Specification spec = new LocationByWorldSpecification(worldId);
                    if (query != null && !query.isEmpty()) {
                        spec = new AndSpecification(spec, new LocationByNameSpecification(query));
                    }
                    return repository.searchLocations(spec);
                })
        );
    }

    public LiveData<List<Location>> getLocations() {
        return locations;
    }

    public void setWorldId(int worldId) {
        worldIdFilter.setValue(worldId);
    }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    public LiveData<Location> getLocationById(int id) {
        return repository.getLocationById(id);
    }

    public LiveData<LocationWithDetails> getLocationWithDetails(int locationId) {
        return repository.locationDao.getLocationWithDetails(locationId);
    }

    public void insert(Location location) {
        repository.insert(location);
    }

    public void update(Location location) {
        repository.update(location);
    }

    public void delete(Location location) {
        repository.delete(location);
    }

    public LiveData<List<Location>> getLocationsForWorld(int worldId) {
        return repository.getLocationsForWorld(worldId);
    }
}