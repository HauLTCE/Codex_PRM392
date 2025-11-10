package com.hault.codex_java.ui.event;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.hault.codex_java.R;
import com.hault.codex_java.data.model.Character;
import com.hault.codex_java.data.model.Location;
import com.hault.codex_java.data.model.crossref.EventCharacterCrossRef;
import com.hault.codex_java.data.model.crossref.EventLocationCrossRef;
import com.hault.codex_java.data.model.relations.EventWithDetails;
import com.hault.codex_java.ui.character.CharacterListAdapter;
import com.hault.codex_java.ui.dialog.ItemRoleSelectorDialogFragment;
import com.hault.codex_java.ui.location.LocationListAdapter;
import com.hault.codex_java.viewmodel.CharacterViewModel;
import com.hault.codex_java.viewmodel.CrossRefViewModel;
import com.hault.codex_java.viewmodel.EventViewModel;
import com.hault.codex_java.viewmodel.LocationViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EventDetailFragment extends Fragment {

    private static final String ARG_EVENT_ID = "event_id";
    private static final String REQUEST_KEY_SELECT_CHARS = "requestKey_event_selectChars";
    private static final String REQUEST_KEY_SELECT_LOCATIONS = "requestKey_event_selectLocations";
    private int eventId;

    private EventViewModel eventViewModel;
    private CharacterViewModel characterViewModel;
    private LocationViewModel locationViewModel;
    private CrossRefViewModel crossRefViewModel;
    private EventWithDetails currentEventDetails;

    private MaterialToolbar toolbar;
    private ImageView imageViewHeader;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView tvDate, tvDescription;
    private RecyclerView rvParticipants, rvLocations;
    private CharacterListAdapter participantsAdapter;
    private LocationListAdapter locationAdapter;
    private ImageButton btnAddParticipant, btnAddLocation;

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
        characterViewModel = new ViewModelProvider(this).get(CharacterViewModel.class);
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        crossRefViewModel = new ViewModelProvider(this).get(CrossRefViewModel.class);

        setupFragmentResultListeners();
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
        setupClickListeners();

        eventViewModel.getEventWithDetails(eventId).observe(getViewLifecycleOwner(), eventWithDetails -> {
            if (eventWithDetails != null) {
                this.currentEventDetails = eventWithDetails;
                updateUI();
            }
        });
    }

    private void bindViews(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        imageViewHeader = view.findViewById(R.id.imageViewHeader);
        appBarLayout = view.findViewById(R.id.app_bar_layout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) appBarLayout.getChildAt(0);
        tvDate = view.findViewById(R.id.textViewDate);
        tvDescription = view.findViewById(R.id.textViewDescription);
        rvParticipants = view.findViewById(R.id.recyclerViewParticipants);
        rvLocations = view.findViewById(R.id.recyclerViewLocations);
        btnAddParticipant = view.findViewById(R.id.btnAddParticipant);
        btnAddLocation = view.findViewById(R.id.btnAddLocation);
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

        if (currentEventDetails.event.imageUri != null && !currentEventDetails.event.imageUri.isEmpty()) {
            Glide.with(this)
                    .load(Uri.parse(currentEventDetails.event.imageUri))
                    .centerCrop()
                    .into(imageViewHeader);
        }

        if (currentEventDetails.event.colorHex != null && !currentEventDetails.event.colorHex.isEmpty()) {
            int color = Color.parseColor(currentEventDetails.event.colorHex);
            collapsingToolbarLayout.setContentScrimColor(color);
            appBarLayout.setBackgroundColor(color);
        }
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

    private void setupClickListeners() {
        btnAddParticipant.setOnClickListener(v -> openCharacterSelector());
        btnAddLocation.setOnClickListener(v -> openLocationSelector());
    }

    private void openCharacterSelector() {
        if (currentEventDetails == null) return;
        int worldId = currentEventDetails.event.worldId;

        characterViewModel.getCharactersForWorld(worldId).observe(getViewLifecycleOwner(), new Observer<List<Character>>() {
            @Override
            public void onChanged(List<Character> characters) {
                characterViewModel.getCharactersForWorld(worldId).removeObserver(this);
                ArrayList<String> names = new ArrayList<>();
                ArrayList<Integer> ids = new ArrayList<>();
                for(Character character : characters) {
                    names.add(character.name);
                    ids.add(character.id);
                }
                ItemRoleSelectorDialogFragment.newInstance("Add Participants", names, ids, REQUEST_KEY_SELECT_CHARS, "Role in event")
                        .show(getParentFragmentManager(), ItemRoleSelectorDialogFragment.TAG);
            }
        });
    }

    private void openLocationSelector() {
        if (currentEventDetails == null) return;
        int worldId = currentEventDetails.event.worldId;

        locationViewModel.getLocationsForWorld(worldId).observe(getViewLifecycleOwner(), new Observer<List<Location>>() {
            @Override
            public void onChanged(List<Location> locations) {
                locationViewModel.getLocationsForWorld(worldId).removeObserver(this);
                ArrayList<String> names = new ArrayList<>();
                ArrayList<Integer> ids = new ArrayList<>();
                for(Location location : locations) {
                    names.add(location.name);
                    ids.add(location.id);
                }
                ItemRoleSelectorDialogFragment.newInstance("Add Locations", names, ids, REQUEST_KEY_SELECT_LOCATIONS, null)
                        .show(getParentFragmentManager(), ItemRoleSelectorDialogFragment.TAG);
            }
        });
    }

    private void setupFragmentResultListeners() {
        getParentFragmentManager().setFragmentResultListener(REQUEST_KEY_SELECT_CHARS, this, (requestKey, bundle) -> {
            ArrayList<Integer> selectedIds = bundle.getIntegerArrayList(ItemRoleSelectorDialogFragment.RESULT_KEY_SELECTED_IDS);
            String role = bundle.getString(ItemRoleSelectorDialogFragment.RESULT_KEY_ROLE);
            if (selectedIds != null && role != null) {
                for (Integer charId : selectedIds) {
                    EventCharacterCrossRef crossRef = new EventCharacterCrossRef(eventId, charId, role);
                    crossRefViewModel.insert(crossRef);
                }
            }
        });
        getParentFragmentManager().setFragmentResultListener(REQUEST_KEY_SELECT_LOCATIONS, this, (requestKey, bundle) -> {
            ArrayList<Integer> selectedIds = bundle.getIntegerArrayList(ItemRoleSelectorDialogFragment.RESULT_KEY_SELECTED_IDS);
            if (selectedIds != null) {
                for (Integer locId : selectedIds) {
                    EventLocationCrossRef crossRef = new EventLocationCrossRef(eventId, locId);
                    crossRefViewModel.insert(crossRef);
                }
            }
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