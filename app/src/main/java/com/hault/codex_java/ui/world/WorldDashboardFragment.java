package com.hault.codex_java.ui.world;

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
import com.hault.codex_java.data.model.relations.WorldWithDetails;
import com.hault.codex_java.viewmodel.WorldViewModel;
import dagger.hilt.android.AndroidEntryPoint;
import java.util.Locale;

@AndroidEntryPoint
public class WorldDashboardFragment extends Fragment {

    private static final String ARG_WORLD_ID = "world_id";
    private int worldId;

    private WorldViewModel worldViewModel;
    private WorldWithDetails currentWorldWithDetails;

    private MaterialToolbar toolbar;
    private TextView tvDescription, tvCharacterCount, tvLocationCount, tvEventCount, tvArcCount;
    private Button btnViewCharacters, btnViewLocations, btnViewEvents, btnViewArcs;

    public static WorldDashboardFragment newInstance(int worldId) {
        WorldDashboardFragment fragment = new WorldDashboardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_WORLD_ID, worldId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            worldId = getArguments().getInt(ARG_WORLD_ID);
        }
        worldViewModel = new ViewModelProvider(this).get(WorldViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_world_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindViews(view);
        setupToolbar();
        setupNavigation();

        worldViewModel.getWorldWithDetails(worldId).observe(getViewLifecycleOwner(), worldWithDetails -> {
            if (worldWithDetails != null) {
                this.currentWorldWithDetails = worldWithDetails;
                updateUI();
            }
        });
    }

    private void bindViews(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        tvDescription = view.findViewById(R.id.textViewWorldDescription);
        tvCharacterCount = view.findViewById(R.id.textViewCharacterCount);
        tvLocationCount = view.findViewById(R.id.textViewLocationCount);
        tvEventCount = view.findViewById(R.id.textViewEventCount);
        tvArcCount = view.findViewById(R.id.textViewArcCount);
        btnViewCharacters = view.findViewById(R.id.btnViewAllCharacters);
        btnViewLocations = view.findViewById(R.id.btnViewAllLocations);
        btnViewEvents = view.findViewById(R.id.btnViewAllEvents);
        btnViewArcs = view.findViewById(R.id.btnViewAllArcs);
    }

    private void updateUI() {
        toolbar.setTitle(currentWorldWithDetails.world.name);
        tvDescription.setText(currentWorldWithDetails.world.description);

        tvCharacterCount.setText(String.format(Locale.getDefault(), "%d", currentWorldWithDetails.characters.size()));
        tvLocationCount.setText(String.format(Locale.getDefault(), "%d", currentWorldWithDetails.locations.size()));
        tvEventCount.setText(String.format(Locale.getDefault(), "%d", currentWorldWithDetails.events.size()));
        tvArcCount.setText(String.format(Locale.getDefault(), "%d", currentWorldWithDetails.arcs.size()));
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
        btnViewCharacters.setOnClickListener(v -> navigateToDetail(0));
        btnViewLocations.setOnClickListener(v -> navigateToDetail(1));
        btnViewEvents.setOnClickListener(v -> navigateToDetail(2));
        btnViewArcs.setOnClickListener(v -> navigateToDetail(3));
    }

    private void navigateToDetail(int tabIndex) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, WorldDetailFragment.newInstance(worldId, tabIndex))
                .addToBackStack(null)
                .commit();
    }

    private void navigateToEdit() {
        if (currentWorldWithDetails != null) {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, EditWorldFragment.newInstance(worldId))
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void confirmDelete() {
        if (currentWorldWithDetails == null) return;
        new AlertDialog.Builder(getContext())
                .setTitle("Delete World")
                .setMessage("Are you sure you want to delete '" + currentWorldWithDetails.world.name + "' and all its contents? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    worldViewModel.delete(currentWorldWithDetails.world);
                    getParentFragmentManager().popBackStack();
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}