package com.hault.codex_java.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import com.hault.codex_java.data.local.specification.AndSpecification;
import com.hault.codex_java.data.local.specification.ChapterByArcSpecification;
import com.hault.codex_java.data.local.specification.ChapterByTitleSpecification;
import com.hault.codex_java.data.local.specification.Specification;
import com.hault.codex_java.data.model.Chapter;
import com.hault.codex_java.data.model.relations.ChapterWithDetails;
import com.hault.codex_java.data.repository.ChapterRepository;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ChapterViewModel extends ViewModel {
    private final ChapterRepository repository;
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    private final MutableLiveData<Integer> arcIdFilter = new MutableLiveData<>();

    private final LiveData<List<Chapter>> chapters;

    @Inject
    public ChapterViewModel(ChapterRepository repository) {
        this.repository = repository;

        chapters = Transformations.switchMap(arcIdFilter, arcId ->
                Transformations.switchMap(searchQuery, query -> {
                    Specification spec = new ChapterByArcSpecification(arcId);
                    if (query != null && !query.isEmpty()) {
                        spec = new AndSpecification(spec, new ChapterByTitleSpecification(query));
                    }
                    return repository.searchChapters(spec);
                })
        );
    }

    public LiveData<List<Chapter>> getChapters() {
        return chapters;
    }

    public void setArcId(int arcId) {
        arcIdFilter.setValue(arcId);
    }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    public LiveData<Chapter> getChapterById(int id) {
        return repository.getChapter(id);
    }

    public LiveData<ChapterWithDetails> getChapterWithDetails(int chapterId) {
        return repository.getChapterWithDetails(chapterId);
    }

    public void insert(Chapter chapter) {
        repository.insert(chapter);
    }

    public void update(Chapter chapter) {
        repository.update(chapter);
    }

    public void delete(Chapter chapter) {
        repository.delete(chapter);
    }
}