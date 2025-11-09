package com.hault.codex_java.ui.arc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.appbar.MaterialToolbar;
import com.hault.codex_java.R;
import com.hault.codex_java.data.model.Chapter;
import com.hault.codex_java.data.model.relations.ArcWithDetails;
import com.hault.codex_java.ui.chapter.ChapterListFragment;
import com.hault.codex_java.viewmodel.ArcViewModel;
import dagger.hilt.android.AndroidEntryPoint;
import java.text.NumberFormat;
import java.util.Locale;

@AndroidEntryPoint
public class ArcDashboardFragment extends Fragment {

    private static final String ARG_WORLD_ID = "world_id";
    private static final String ARG_ARC_ID = "arc_id";

    private int worldId;
    private int arcId;

    private ArcViewModel arcViewModel;
    private ArcWithDetails currentArcWithDetails;

    private MaterialToolbar toolbar;
    private TextView tvSynopsis, tvChapterCount, tvWordCount;
    private Button btnViewChapters;

    public static ArcDashboardFragment newInstance(int worldId, int arcId) {
        ArcDashboardFragment fragment = new ArcDashboardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_WORLD_ID, worldId);
        args.putInt(ARG_ARC_ID, arcId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            worldId = getArguments().getInt(ARG_WORLD_ID);
            arcId = getArguments().getInt(ARG_ARC_ID);
        }
        arcViewModel = new ViewModelProvider(this).get(ArcViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_arc_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindViews(view);
        setupToolbar();

        arcViewModel.getArcWithDetails(arcId).observe(getViewLifecycleOwner(), arcWithDetails -> {
            if (arcWithDetails != null) {
                this.currentArcWithDetails = arcWithDetails;
                updateUI();
                setupNavigation();
            }
        });
    }

    private void bindViews(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        tvSynopsis = view.findViewById(R.id.textViewArcSynopsis);
        tvChapterCount = view.findViewById(R.id.textViewChapterCount);
        tvWordCount = view.findViewById(R.id.textViewWordCount);
        btnViewChapters = view.findViewById(R.id.btnViewAllChapters);
    }

    private void updateUI() {
        toolbar.setTitle(currentArcWithDetails.arc.title);
        tvSynopsis.setText(currentArcWithDetails.arc.synopsis);

        int chapterCount = currentArcWithDetails.chapters.size();
        int totalWordCount = 0;
        for (Chapter chapter : currentArcWithDetails.chapters) {
            totalWordCount += chapter.wordCount;
        }

        tvChapterCount.setText(String.format(Locale.getDefault(), "%d", chapterCount));
        tvWordCount.setText(NumberFormat.getInstance().format(totalWordCount));
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

    private void setupNavigation() {
        btnViewChapters.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, ChapterListFragment.newInstance(worldId, arcId, currentArcWithDetails.arc.title))
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void navigateToEdit() {
        if (currentArcWithDetails != null) {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, EditArcFragment.newInstance(arcId))
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void confirmDelete() {
        if (currentArcWithDetails == null) return;
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Arc")
                .setMessage("Are you sure you want to delete '" + currentArcWithDetails.arc.title + "' and all its chapters? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    arcViewModel.delete(currentArcWithDetails.arc);
                    getParentFragmentManager().popBackStack();
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}