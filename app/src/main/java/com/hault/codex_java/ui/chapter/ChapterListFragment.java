package com.hault.codex_java.ui.chapter;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.hault.codex_java.R;
import com.hault.codex_java.viewmodel.ChapterViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChapterListFragment extends Fragment {

    private static final String ARG_WORLD_ID = "world_id";
    private static final String ARG_ARC_ID = "arc_id";
    private static final String ARG_ARC_TITLE = "arc_title";

    private int worldId;
    private int arcId;
    private String arcTitle;
    private ChapterViewModel chapterViewModel;
    private ChapterListAdapter adapter;
    private TextInputEditText searchEditText;

    public static ChapterListFragment newInstance(int worldId, int arcId, String arcTitle) {
        ChapterListFragment fragment = new ChapterListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_WORLD_ID, worldId);
        args.putInt(ARG_ARC_ID, arcId);
        args.putString(ARG_ARC_TITLE, arcTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            worldId = getArguments().getInt(ARG_WORLD_ID);
            arcId = getArguments().getInt(ARG_ARC_ID);
            arcTitle = getArguments().getString(ARG_ARC_TITLE);
        }
        chapterViewModel = new ViewModelProvider(this).get(ChapterViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chapter_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chapterViewModel.setArcId(arcId);

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(arcTitle);
        toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewChapters);
        adapter = new ChapterListAdapter();
        recyclerView.setAdapter(adapter);

        searchEditText = view.findViewById(R.id.search_edit_text);

        chapterViewModel.getChapters().observe(getViewLifecycleOwner(), chapters -> adapter.submitList(chapters));

        FloatingActionButton fab = view.findViewById(R.id.fabAddChapter);
        fab.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, AddChapterFragment.newInstance(worldId, arcId))
                    .addToBackStack(null)
                    .commit();
        });

        adapter.setOnItemClickListener(chapter -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, ChapterDetailFragment.newInstance(chapter.id))
                    .addToBackStack(null)
                    .commit();
        });

        setupSearch();
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                chapterViewModel.setSearchQuery(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}