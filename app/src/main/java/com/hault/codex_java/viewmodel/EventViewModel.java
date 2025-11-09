package com.hault.codex_java.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.hault.codex_java.data.local.specification.AndSpecification;
import com.hault.codex_java.data.local.specification.EventByNameSpecification;
import com.hault.codex_java.data.local.specification.EventByWorldSpecification;
import com.hault.codex_java.data.local.specification.Specification;
import com.hault.codex_java.data.model.Event;
import com.hault.codex_java.data.model.relations.EventWithDetails;
import com.hault.codex_java.data.repository.EventRepository;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class EventViewModel extends ViewModel {
    private final EventRepository repository;

    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    private final MutableLiveData<Integer> worldIdFilter = new MutableLiveData<>();

    private final LiveData<List<Event>> events;

    @Inject
    public EventViewModel(EventRepository repository) {
        this.repository = repository;

        events = Transformations.switchMap(worldIdFilter, worldId ->
                Transformations.switchMap(searchQuery, query -> {
                    Specification spec = new EventByWorldSpecification(worldId);
                    if (query != null && !query.isEmpty()) {
                        spec = new AndSpecification(spec, new EventByNameSpecification(query));
                    }
                    return repository.searchEvents(spec);
                })
        );
    }

    public LiveData<List<Event>> getEvents() {
        return events;
    }

    public void setWorldId(int worldId) {
        worldIdFilter.setValue(worldId);
    }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    public LiveData<Event> getEventById(int id) {
        return repository.getEvent(id);
    }

    public LiveData<EventWithDetails> getEventWithDetails(int eventId) {
        return repository.eventDao.getEventWithDetails(eventId);
    }

    public void insert(Event event) {
        repository.insert(event);
    }

    public void update(Event event) {
        repository.update(event);
    }

    public void delete(Event event) {
        repository.delete(event);
    }
}