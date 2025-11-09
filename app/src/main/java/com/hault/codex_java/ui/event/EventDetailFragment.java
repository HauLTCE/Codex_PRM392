package com.hault.codex_java.ui.event;

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
import com.hault.codex_java.data.model.relations.EventWithDetails;
import com.hault.codex_java.ui.character.CharacterListAdapter;
import com.hault.codex_java.ui.location.LocationListAdapter;
import com.hault.codex_java.viewmodel.EventViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EventDetailFragment extends Fragment {

    private static final String ARG_EVENT_ID = "event_id";
    private int eventId;

    private EventViewModel eventViewModel;
    private EventWithDetails currentEventDetails;

    private MaterialToolbar toolbar;
    private TextView tvDate, tvDescription;
    private RecyclerView rvParticipants, rvLocations;
    private CharacterListAdapter participantsAdapter;
    private LocationListAdapter locationAdapter;

    public static EventDetailFragment newInstance(int eventId) {
        EventDetailFragment fragment = new EventDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_EVENT_ID, eventId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventId = getArguments().getInt(ARG_EVENT_ID);
        }
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        setupToolbar();
        setupRecyclerViews();

        eventViewModel.getEventWithDetails(eventId).observe(getViewLifecycleOwner(), eventWithDetails -> {
            if (eventWithDetails != null) {
                this.currentEventDetails = eventWithDetails;
                updateUI();
            }
        });
    }

    private void bindViews(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        tvDate = view.findViewById(R.id.textViewDate);
        tvDescription = view.findViewById(R.id.textViewDescription);
        rvParticipants = view.findViewById(R.id.recyclerViewParticipants);
        rvLocations = view.findViewById(R.id.recyclerViewLocations);
    }

    private void setupRecyclerViews() {
        participantsAdapter = new CharacterListAdapter();
        rvParticipants.setAdapter(participantsAdapter);

        locationAdapter = new LocationListAdapter();
        rvLocations.setAdapter(locationAdapter);
    }

    private void updateUI() {
        toolbar.setTitle(currentEventDetails.event.name);
        tvDate.setText(currentEventDetails.event.date);
        tvDescription.setText(currentEventDetails.event.description);
        participantsAdapter.submitList(currentEventDetails.characters);
        locationAdapter.submitList(currentEventDetails.locations);
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
        if (currentEventDetails != null) {
            int worldId = currentEventDetails.event.worldId;
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, EditEventFragment.newInstance(worldId, eventId))
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void confirmDelete() {
        if (currentEventDetails == null) return;
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Event")
                .setMessage("Are you sure you want to delete '" + currentEventDetails.event.name + "'? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    eventViewModel.delete(currentEventDetails.event);
                    getParentFragmentManager().popBackStack();
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}