package com.hault.codex_java.ui.location;

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
import com.hault.codex_java.data.model.relations.LocationWithDetails;
import com.hault.codex_java.ui.character.CharacterListAdapter;
import com.hault.codex_java.ui.event.EventListAdapter;
import com.hault.codex_java.viewmodel.LocationViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LocationDetailFragment extends Fragment {

    private static final String ARG_LOCATION_ID = "location_id";
    private int locationId;

    private LocationViewModel locationViewModel;
    private LocationWithDetails currentLocationDetails;

    private MaterialToolbar toolbar;
    private TextView tvDescription;
    private RecyclerView rvResidents, rvEvents;
    private CharacterListAdapter residentsAdapter;
    private EventListAdapter eventAdapter;

    public static LocationDetailFragment newInstance(int locationId) {
        LocationDetailFragment fragment = new LocationDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LOCATION_ID, locationId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            locationId = getArguments().getInt(ARG_LOCATION_ID);
        }
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        setupToolbar();
        setupRecyclerViews();

        locationViewModel.getLocationWithDetails(locationId).observe(getViewLifecycleOwner(), locationWithDetails -> {
            if (locationWithDetails != null) {
                this.currentLocationDetails = locationWithDetails;
                updateUI();
            }
        });
    }

    private void bindViews(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        tvDescription = view.findViewById(R.id.textViewDescription);
        rvResidents = view.findViewById(R.id.recyclerViewResidents);
        rvEvents = view.findViewById(R.id.recyclerViewEvents);
    }

    private void setupRecyclerViews() {
        residentsAdapter = new CharacterListAdapter();
        rvResidents.setAdapter(residentsAdapter);

        eventAdapter = new EventListAdapter();
        rvEvents.setAdapter(eventAdapter);
    }

    private void updateUI() {
        toolbar.setTitle(currentLocationDetails.location.name);
        tvDescription.setText(currentLocationDetails.location.description);
        residentsAdapter.submitList(currentLocationDetails.residents);
        eventAdapter.submitList(currentLocationDetails.events);
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
        if (currentLocationDetails != null) {
            int worldId = currentLocationDetails.location.worldId;
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, EditLocationFragment.newInstance(worldId, locationId))
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void confirmDelete() {
        if (currentLocationDetails == null) return;
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Location")
                .setMessage("Are you sure you want to delete '" + currentLocationDetails.location.name + "'? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    locationViewModel.delete(currentLocationDetails.location);
                    getParentFragmentManager().popBackStack();
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}