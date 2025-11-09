package com.hault.codex_java.ui.chapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.hault.codex_java.R;
import com.hault.codex_java.data.model.relations.ChapterWithDetails;
import com.hault.codex_java.ui.character.CharacterListAdapter;
import com.hault.codex_java.ui.location.LocationListAdapter;
import com.hault.codex_java.viewmodel.ChapterViewModel;
import dagger.hilt.android.AndroidEntryPoint;
import java.util.Locale;

@AndroidEntryPoint
public class ChapterDetailFragment extends Fragment {

    private static final String ARG_CHAPTER_ID = "chapter_id";
    private int chapterId;

    private ChapterViewModel chapterViewModel;
    private ChapterWithDetails currentChapterDetails;

    private MaterialToolbar toolbar;
    private TextView tvProse;
    private RecyclerView rvCharacters, rvLocations;
    private CharacterListAdapter characterAdapter;
    private LocationListAdapter locationAdapter;

    public static ChapterDetailFragment newInstance(int chapterId) {
        ChapterDetailFragment fragment = new ChapterDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CHAPTER_ID, chapterId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            chapterId = getArguments().getInt(ARG_CHAPTER_ID);
        }
        chapterViewModel = new ViewModelProvider(this).get(ChapterViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chapter_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        setupToolbar();
        setupRecyclerViews();

        chapterViewModel.getChapterWithDetails(chapterId).observe(getViewLifecycleOwner(), chapterWithDetails -> {
            if (chapterWithDetails != null) {
                this.currentChapterDetails = chapterWithDetails;
                updateUI();
            }
        });
    }

    private void bindViews(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        tvProse = view.findViewById(R.id.textViewProse);
        rvCharacters = view.findViewById(R.id.recyclerViewCharacters);
        rvLocations = view.findViewById(R.id.recyclerViewLocations);
    }

    private void setupRecyclerViews() {
        characterAdapter = new CharacterListAdapter();
        rvCharacters.setAdapter(characterAdapter);

        locationAdapter = new LocationListAdapter();
        rvLocations.setAdapter(locationAdapter);
    }

    private void updateUI() {
        toolbar.setTitle(currentChapterDetails.chapter.title);
        toolbar.setSubtitle("Chapter " + currentChapterDetails.chapter.chapterNumber);
        tvProse.setText(currentChapterDetails.chapter.prose);
        characterAdapter.submitList(currentChapterDetails.characters);
        locationAdapter.submitList(currentChapterDetails.locations);
    }

    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());
        toolbar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_edit) {
                navigateToEdit();
                return true;
            } else if (itemId == R.id.action_delete) {
                confirmDelete();
                return true;
            }
            return false;
        });
    }

    private void navigateToEdit() {
        if (currentChapterDetails != null) {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, EditChapterFragment.newInstance(chapterId))
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void confirmDelete() {
        if (currentChapterDetails == null) return;
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Chapter")
                .setMessage("Are you sure you want to delete '" + currentChapterDetails.chapter.title + "'? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    chapterViewModel.delete(currentChapterDetails.chapter);
                    getParentFragmentManager().popBackStack();
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}